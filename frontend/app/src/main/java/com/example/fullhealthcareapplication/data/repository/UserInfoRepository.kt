package com.example.fullhealthcareapplication.data.repository

import android.util.Log
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class LoginResponse(
    val token: String,
    val nric: String,
    val role: String,
    val email: String,
    val fullName: String
)

data class UpdateResponse(
    val email: String,
    val fullName: String
)

class UserInfoRepository (private val tokenDataStore: TokenDataStore){

    private val baseUrl = "https://f5fqqafe6e.execute-api.us-east-1.amazonaws.com"

    suspend fun userSignUp(
        nric: String,
        role: String,
        email: String,
        fullname: String,
        password: String
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/user-sign-up")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonRequest = JSONObject().apply {
                    put("nric", nric)
                    put("role", role)
                    put("email", email)
                    put("fullname", fullname)
                    put("password", password)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonRequest.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    return@withContext "User Added"
                } else {
                    Log.e("userInfoRepository", "Sign up failed: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("UserInfoRepository", "Error during sign up", e)
                return@withContext null
            }
        }
    }

    suspend fun userLogin(
        nric: String,
        password: String
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/user-login")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonObject = JSONObject().apply {
                    put("nric", nric)
                    put("password", password)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    val loginResponse = LoginResponse(
                        token = jsonResponse.getString("token"),
                        nric = jsonResponse.getString("nric"),
                        role = jsonResponse.getString("role"),
                        email = jsonResponse.getString("email"),
                        fullName = jsonResponse.getString("fullname")
                    )

                    tokenDataStore.saveToken(loginResponse.token)
                    tokenDataStore.saveNric(loginResponse.nric)
                    tokenDataStore.saveRole(loginResponse.role)
                    tokenDataStore.saveEmail(loginResponse.email)
                    tokenDataStore.saveFullName(loginResponse.fullName)

                    Log.d("Saved Token", "${tokenDataStore.getToken.first()}")

                    return@withContext loginResponse
                } else {
                    Log.e("UserInfoRepository", "Login failed: $responseCode")
                }
            } catch (e:Exception) {
                Log.e("UserInfoRepository", "Error during login: ${e.localizedMessage}", e)
                return@withContext null
            }
        }
    }

    suspend fun userUpdate(
        nric: String,
        role: String,
        email: String,
        fullname: String,
    ): Any? {
        return withContext(Dispatchers.IO){
            try {
                val url = URL("$baseUrl/user-profile")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("User Info Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val jsonRequest = JSONObject().apply {
                    put("nric", nric)
                    put("role", role)
                    put("email", email)
                    put("fullname", fullname)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonRequest.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)
                    val updatedValues = jsonResponse.getJSONObject("updatedAttributes")
                    val updateResponse = UpdateResponse(
                        email = updatedValues.getString("email"),
                        fullName = updatedValues.getString("fullname")
                    )

                    tokenDataStore.editUserProfile(
                        updateResponse.email,
                        updateResponse.fullName
                    )


                    Log.d("New Email", "${tokenDataStore.getEmail.first()}")
                    Log.d("New Name", "${tokenDataStore.getFullName.first()}")
                    return@withContext "User Updated"
                } else {
                    Log.e("userInfoRepository", "Update failed: $responseCode")
                    return@withContext null
                }

            }catch (e: Exception) {
                Log.e("UserInfoRepository", "Error during update: ${e.localizedMessage}", e)
                return@withContext null
            }
        }
    }

    suspend fun userChangePassword(
        nric: String,
        role: String,
        password: String,
        newPassword: String
    ): Any? {
        return withContext(Dispatchers.IO){
            try {
                val url = URL("$baseUrl/user-password")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("User Info Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val jsonRequest = JSONObject().apply {
                    put("nric", nric)
                    put("role", role)
                    put("password", password)
                    put("newpassword", newPassword)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonRequest.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return@withContext "Changed Password"
                } else {
                    Log.e("userInfoRepository", "New password failed: $responseCode")
                    return@withContext null
                }

            }catch (e: Exception) {
                Log.e("UserInfoRepository", "Error changing password: ${e.localizedMessage}", e)
                return@withContext null
            }
        }
    }

    suspend fun userDeleteAccount(
        nric: String,
        role: String,
        password: String,
    ): Any? {
        return withContext(Dispatchers.IO){
            try {
                val url = URL("$baseUrl/user-profile")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("User Info Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val jsonRequest = JSONObject().apply {
                    put("nric", nric)
                    put("role", role)
                    put("password", password)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonRequest.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return@withContext "Account Deleted"
                } else {
                    Log.e("userInfoRepository", "Delete Account failed: $responseCode")
                    return@withContext null
                }

            }catch (e: Exception) {
                Log.e("UserInfoRepository", "Error deleting account: ${e.localizedMessage}", e)
                return@withContext null
            }
        }
    }

}