package com.example.fullhealthcareapplication.data.repository

import android.content.Context
import android.util.Log
import com.example.fullhealthcareapplication.data.database.HealthServiceDatabase
import com.example.fullhealthcareapplication.data.entity.Activity
import com.example.fullhealthcareapplication.data.entity.Category
import com.example.fullhealthcareapplication.data.entity.Medication
import com.example.fullhealthcareapplication.data.entity.Time
import com.example.fullhealthcareapplication.data.entity.User
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class IdResponse(
    val id: Int,
    val nric: String,
    val role: String,
    val age: Int,
    val gender: String,
    val weight: Double,
    val height: Double
)

data class CategoryResponse(
    val id: Int,
    val name: String
)

data class TimeResponse(
    val id: Int,
    val time: String
)

data class MedicationResponse(
    val id: Int,
    val userId: Int,
    val timeId: Int,
    val name: String,
    val type: String,
    val measureAmount: Double,
    val measureUnit: String,
    val frequency: String
)

data class ActivityResponse(
    val id: Int,
    val userId: Int,
    val categoryId: Int,
    val timeTaken: String,
    val caloriesBurnt: Double,
    val stepCount: Double,
    val distance: Double,
    val walkingSpeed: Double,
    val walkingSteadiness: Double
)

class HealthServiceRepository(
    private val tokenDataStore: TokenDataStore,
    private val context: Context
){
    private val database = HealthServiceDatabase.getDatabase(context)
    private val userDao = database.userDao
    private val activityDao = database.activityDao
    private val medicationDao = database.medicationDao
    private val categoryDao = database.categoryDao
    private val timeDao = database.timeDao

    private val baseUrl = "https://f5fqqafe6e.execute-api.us-east-1.amazonaws.com"

    suspend fun getUserId(
        nric: String
    ): Result<IdResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/info/$nric")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }

                    val jsonResponse = JSONObject(response)
                    val idResponse = IdResponse( // To be stored into the local database
                        id = jsonResponse.getInt("id"),
                        nric = jsonResponse.getString("nric"),
                        role = jsonResponse.getString("role"),
                        age = jsonResponse.getInt("age"),
                        gender = jsonResponse.getString("gender"),
                        weight = jsonResponse.getDouble("weight"),
                        height = jsonResponse.getDouble("height"),
                    )

                    val user = User(
                        id = idResponse.id,
                        nric = idResponse.nric,
                        role = idResponse.role,
                        age = idResponse.age,
                        gender = idResponse.gender,
                        weight = idResponse.weight,
                        height = idResponse.height
                    )
                    userDao.insertUser(user)
                    // user is inserted into the database only when it has successfully obtained it from the remote server
                    val cachedUser = userDao.getUser(user.id)
                    tokenDataStore.saveId(idResponse.id)
                    Log.d("Obtained User Details", "$idResponse")
                    Log.d("Stored User ID ", "${tokenDataStore.getId.first()}")

                    if (cachedUser != null) {
                        val idResponseFromCache = IdResponse(
                            id = cachedUser.id,
                            nric = cachedUser.nric,
                            role = cachedUser.role,
                            age = cachedUser.age,
                            gender = cachedUser.gender,
                            weight = cachedUser.weight,
                            height = cachedUser.height
                        )
                        Log.d("HealthServiceRepo", "Successfully Cached: $idResponseFromCache")
                        return@withContext Result.Success(idResponseFromCache)
                    } else {
                        return@withContext Result.Error(Exception("User not found in cache"))
                    }
                } else {
                    Log.e("HealthServiceRepository", "Failed to get: $responseCode")
                    return@withContext Result.Error(Exception("Failed to get user: $responseCode"))
                }
            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error getting user ID: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun getUserMeasurements(
        id: Int
    ): User? {
        return withContext(Dispatchers.IO) {
            return@withContext userDao.getUser(id)
        }
    }

    suspend fun editUserMeasurements(
        id: Int,
        nric: String,
        role: String,
        age: Int,
        gender: String,
        weight: Double,
        height: Double
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/user/$id")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonObject = JSONObject().apply {
                    put("nric", nric)
                    put("role", role)
                    put("age", age)
                    put("gender", gender)
                    put("weight", weight)
                    put("height", height)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val user = User(
                        id = id,
                        nric = nric,
                        role = role,
                        age = age,
                        gender = gender,
                        weight = weight,
                        height = height
                    )
                    userDao.insertUser(user)
                    Log.d("Repo", "Edited Measurements !")
                    return@withContext "Measurements Edited"
                } else {
                    Log.e("HealthServiceRepository", "Failed to edit measurements: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error editing measurements: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun addUserMeasurements(
        nric: String,
        role: String,
        age: Int,
        gender: String,
        weight: Double,
        height: Double
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/user")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonObject = JSONObject().apply {
                    put("nric", nric)
                    put("role", role)
                    put("age", age)
                    put("gender", gender)
                    put("weight", weight)
                    put("height", height)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("Deed", "Has Been Done")
                    return@withContext "User Measurements Added"
                } else {
                    Log.e("HealthServiceRepository", "Failed to add: $responseCode")
                    return@withContext Result.Error(Exception("Failed to add measurements: $responseCode"))
                }
            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error adding activity: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }


    suspend fun addActivity(
        userId: Int,
        activityCategoryId: Int,
        timeTaken: String,
        caloriesBurnt: Double,
        stepCount: Double,
        distance: Double,
        walkingSpeed: Double,
        walkingSteadiness: Double
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/activity")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonObject = JSONObject().apply {
                    put("user_id", userId)
                    put("category_id", activityCategoryId)
                    put("time_taken", timeTaken)
                    put("calories_burnt", caloriesBurnt)
                    put("step_count", stepCount)
                    put("distance", distance)
                    put("walking_speed", walkingSpeed)
                    put("walking_steadiness", walkingSteadiness)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return@withContext "Activity Added"
                } else {
                    Log.e("HealthServiceRepository", "Failed to add: $responseCode")
                    return@withContext Result.Error(Exception("Failed to add activity: $responseCode"))
                }
            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error adding activity: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun editActivity(
        id: Int,
        userId: Int,
        activityCategoryId: Int,
        timeTaken: String,
        caloriesBurnt: Double,
        stepCount: Double,
        distance: Double,
        walkingSpeed: Double,
        walkingSteadiness: Double
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("INSIDE EDIT ACTIVITY", "THAT MEANS ITS EDITING")
                Log.d("ID", "$id")
                val url = URL("$baseUrl/activity/$id")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true


                val jsonObject = JSONObject().apply {
                    put("user_id", userId)
                    put("category_id", activityCategoryId)
                    put("time_taken", timeTaken)
                    put("calories_burnt", caloriesBurnt)
                    put("step_count", stepCount)
                    put("distance", distance)
                    put("walking_speed", walkingSpeed)
                    put("walking_steadiness", walkingSteadiness)
                }
                Log.d("EditActivityPayload", jsonObject.toString())

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = StringBuilder()
                    BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                    }
                    val jsonResponse = JSONObject(response.toString())
                    val success = jsonResponse.getBoolean("success")
                    val changedRows = jsonResponse.getInt("changedRows")

                    Log.d("Success?", "Activity Edited: $success, Changed Rows: $changedRows")
                    return@withContext if (success) "Activity Edited" else "Edit Failed"
                } else {
                    Log.e("HealthServiceRepository", "Failed to edit activity: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error editing activity: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun deleteActivity(
        id: Int
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/activity/$id")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    deleteCachedActivity(id)
                    return@withContext "Activity Deleted"
                } else {
                    Log.e("HealthServiceRepository", "Failed to delete activity: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error deleting activity: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun deleteCachedActivity(
        id: Int
    ) {
        return withContext(Dispatchers.IO) {
            return@withContext activityDao.deleteActivity(id)
        }
    }

    suspend fun addMedication(
        userId: Int,
        timeId: Int,
        name: String,
        type: String,
        measureAmount: Double,
        measureUnit: String,
        frequency: String,
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/medication")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonObject = JSONObject().apply {
                    put("user_id", userId)
                    put("time_id", timeId)
                    put("name", name)
                    put("type", type)
                    put("measure_amount", measureAmount)
                    put("measure_unit", measureUnit)
                    put("frequency", frequency)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return@withContext "Medication Added"
                } else {
                    Log.e("HealthServiceRepository", "Failed to add: $responseCode")
                    return@withContext Result.Error(Exception("Failed to add medication: $responseCode"))
                }
            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error adding medication: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun editMedication(
        id: Int,
        userId: Int,
        timeId: Int,
        name: String,
        type: String,
        measureAmount: Double,
        measureUnit: String,
        frequency: String,
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/medication/$id")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true


                val jsonObject = JSONObject().apply {
                    put("user_id", userId)
                    put("time_id", timeId)
                    put("name", name)
                    put("type", type)
                    put("measure_amount", measureAmount)
                    put("measure_unit", measureUnit)
                    put("frequency", frequency)
                }
                Log.d("EditMedicationPayload", jsonObject.toString())

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = StringBuilder()
                    BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                    }
                    val jsonResponse = JSONObject(response.toString())
                    val success = jsonResponse.getBoolean("success")
                    val changedRows = jsonResponse.getInt("changedRows")

                    Log.d("Success?", "Medication Edited: $success, Changed Rows: $changedRows")
                    return@withContext if (success) "Medication Edited" else "Edit Failed"
                } else {
                    Log.e("HealthServiceRepository", "Failed to edit medication: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error editing medication: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun deleteMedication(
        id: Int
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/medication/$id")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    deleteCachedMedication(id)
                    return@withContext "Activity Deleted"
                } else {
                    Log.e("HealthServiceRepository", "Failed to delete medication: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("HealthServiceRepository", "Error deleting medication: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun deleteCachedMedication(
        id: Int
    ) {
        return withContext(Dispatchers.IO) {
            return@withContext medicationDao.deleteMedication(id)
        }
    }

    suspend fun getCategories(): Result<List<CategoryResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/category")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(response)
                    val contentList = mutableListOf<CategoryResponse>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonResponse = jsonArray.getJSONObject(i)
                        val categories = CategoryResponse(
                            id = jsonResponse.getInt("id"),
                            name = jsonResponse.getString("name")
                        )
                        val category = Category(
                            id = categories.id,
                            name = categories.name
                        )
                        contentList.add(categories)
                        categoryDao.insertCategory(category)
                    }
                    val cachedCategories = categoryDao.getAllCategories()
                    val categoryList = mutableListOf<CategoryResponse>()
                    for (i in cachedCategories) {
                        val category = CategoryResponse(
                            id = i.id,
                            name = i.name
                        )
                        categoryList.add(category)
                    }

                    Log.d("HealthServiceRepo", "Successfully cached categories: $categoryList")
                    return@withContext Result.Success(categoryList)
                } else {
                    return@withContext Result.Error(Exception("Category not found in cache"))
                }

            } catch (e: Exception) {
                Log.e(
                    "HealthServiceRepository",
                    "Error getting categories: ${e.localizedMessage}",
                    e
                )
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun getAllCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            return@withContext categoryDao.getAllCategories()
        }
    }

    suspend fun getTimes(): Result<List<TimeResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/time")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(response)
                    val contentList = mutableListOf<TimeResponse>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonResponse = jsonArray.getJSONObject(i)
                        val times = TimeResponse(
                            id = jsonResponse.getInt("id"),
                            time = jsonResponse.getString("time")
                        )
                        val time = Time(
                            id = times.id,
                            time = times.time
                        )
                        contentList.add(times)
                        timeDao.insertTime(time)
                    }
                    val cachedTimes = timeDao.getAllTimes()
                    val timeList = mutableListOf<TimeResponse>()
                    for (i in cachedTimes) {
                        val time = TimeResponse(
                            id = i.id,
                            time = i.time
                        )
                        timeList.add(time)
                    }

                    Log.d("HealthServiceRepo", "Successfully cached times: $timeList")
                    return@withContext Result.Success(timeList)
                } else {
                    return@withContext Result.Error(Exception("Time not found in cache"))
                }

            } catch (e: Exception) {
                Log.e(
                    "HealthServiceRepository",
                    "Error getting categories: ${e.localizedMessage}",
                    e
                )
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun getAllTimes(): List<Time> {
        return withContext(Dispatchers.IO) {
            return@withContext timeDao.getAllTimes()
        }
    }

    suspend fun getMedications(
        userId: Int
    ): Result<List<MedicationResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/medication/$userId")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(response)
                    val contentList = mutableListOf<MedicationResponse>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonResponse = jsonArray.getJSONObject(i)
                        val medications = MedicationResponse(
                            id = jsonResponse.getInt("id"),
                            userId = jsonResponse.getInt("user_id"),
                            timeId = jsonResponse.getInt("time_id"),
                            name = jsonResponse.getString("name"),
                            type = jsonResponse.getString("type"),
                            measureAmount = jsonResponse.getDouble("measure_amount"),
                            measureUnit = jsonResponse.getString("measure_unit"),
                            frequency = jsonResponse.getString("frequency")
                        )
                        val medication = Medication(
                            id = medications.id,
                            userId = medications.userId,
                            timeId = medications.timeId,
                            name = medications.name,
                            type = medications.type,
                            measureAmount = medications.measureAmount,
                            measureUnit = medications.measureUnit,
                            frequency = medications.frequency
                        )
                        contentList.add(medications)
                        medicationDao.addMedication(medication)
                    }
                    val cachedMedications = medicationDao.getAllMedications(userId)
                    val medicationList = mutableListOf<MedicationResponse>()
                    for (i in cachedMedications) {
                        val medication = MedicationResponse(
                            id = i.id,
                            userId = i.userId,
                            timeId = i.timeId,
                            name = i.name,
                            type = i.type,
                            measureAmount = i.measureAmount,
                            measureUnit = i.measureUnit,
                            frequency = i.frequency
                        )
                        medicationList.add(medication)
                    }

                    Log.d("HealthServiceRepo", "Successfully cached medication: $medicationList")
                    return@withContext Result.Success(medicationList)
                } else {
                    return@withContext Result.Error(Exception("Medication not found in cache"))
                }

            } catch (e: Exception) {
                Log.e(
                    "HealthServiceRepository",
                    "Error getting categories: ${e.localizedMessage}",
                    e
                )
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun getAllMedications(
        userId: Int
    ): List<Medication> {
        return withContext(Dispatchers.IO) {
            return@withContext medicationDao.getAllMedications(userId)
        }
    }

    suspend fun getActivities(
        userId: Int,
        date: String
    ): Result<List<ActivityResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/activity/$userId/$date")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Health Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(response)
                    val contentList = mutableListOf<ActivityResponse>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonResponse = jsonArray.getJSONObject(i)
                        val activities = ActivityResponse(
                            id = jsonResponse.getInt("id"),
                            userId = jsonResponse.getInt("user_id"),
                            categoryId = jsonResponse.getInt("category_id"),
                            timeTaken = jsonResponse.getString("time_taken"),
                            caloriesBurnt = jsonResponse.getDouble("calories_burnt"),
                            stepCount = jsonResponse.getDouble("step_count"),
                            distance = jsonResponse.getDouble("distance"),
                            walkingSpeed = jsonResponse.getDouble("walking_speed"),
                            walkingSteadiness = jsonResponse.getDouble("walking_steadiness")
                        )
                        val activity = Activity(
                            id = activities.id,
                            userId = activities.userId,
                            categoryId = activities.categoryId,
                            timeTaken = activities.timeTaken,
                            caloriesBurnt = activities.caloriesBurnt,
                            stepCount = activities.stepCount,
                            distance = activities.distance,
                            walkingSpeed = activities.walkingSpeed,
                            walkingSteadiness = activities.walkingSteadiness
                        )
                        contentList.add(activities)
                        activityDao.addActivity(activity)
                    }
                    val cachedActivities = activityDao.getAllActivities(userId, date)
                    val activityList = mutableListOf<ActivityResponse>()
                    for (i in cachedActivities) {
                        val activity = ActivityResponse(
                            id = i.id,
                            userId = i.userId,
                            categoryId = i.categoryId,
                            timeTaken = i.timeTaken,
                            caloriesBurnt = i.caloriesBurnt,
                            stepCount = i.stepCount,
                            distance = i.distance,
                            walkingSpeed = i.walkingSpeed,
                            walkingSteadiness = i.walkingSteadiness
                        )
                        activityList.add(activity)
                    }

                    Log.d("HealthServiceRepo", "Successfully cached activities: $activityList")
                    return@withContext Result.Success(activityList)
                } else {
                    return@withContext Result.Error(Exception("Activities not found in cache"))
                }

            } catch (e: Exception) {
                Log.e(
                    "HealthServiceRepository",
                    "Error getting categories: ${e.localizedMessage}",
                    e
                )
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun getAllActivities(
        userId: Int,
        date: String
    ): List<Activity> {
        return withContext(Dispatchers.IO) {
            return@withContext activityDao.getAllActivities(userId, date)
        }
    }
}