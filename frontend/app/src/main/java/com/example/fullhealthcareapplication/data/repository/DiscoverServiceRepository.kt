package com.example.fullhealthcareapplication.data.repository

import android.util.Log
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class ContentResponse(
    val id: Int,
    val contentCategoryId: Int,
    val title: String,
    val summary: String,
    val description: String
)

data class ContentCategoriesResponse(
    val id: Int,
    val name: String
)


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class DiscoverServiceRepository(private val tokenDataStore: TokenDataStore) {

    private val baseUrl = "https://f5fqqafe6e.execute-api.us-east-1.amazonaws.com"

    suspend fun getAllContent(): Result<List<ContentResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/content")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Discover Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(response)
                    val contentList = mutableListOf<ContentResponse>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonResponse = jsonArray.getJSONObject(i)
                        val contentResponse = ContentResponse(
                            id = jsonResponse.getInt("id"),
                            contentCategoryId = jsonResponse.getInt("content_category_id"),
                            title = jsonResponse.getString("title"),
                            summary = jsonResponse.getString("summary"),
                            description = jsonResponse.getString("description")
                        )
                        contentList.add(contentResponse)
                        Log.d("Got Content !!", "$contentResponse")
                    }

                    return@withContext Result.Success(contentList)
                } else {
                    Log.e("DiscoverServiceRepository", "Failed to get: $responseCode")
                    return@withContext Result.Error(Exception("Failed to get content: $responseCode"))
                }
            } catch (e: Exception) {
                Log.e("DiscoverServiceRepository", "Error getting content: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun getAllContentCategories(): Result<List<ContentCategoriesResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/content-category")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Discover Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(response)
                    val contentList = mutableListOf<ContentCategoriesResponse>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonResponse = jsonArray.getJSONObject(i)
                        val contentResponse = ContentCategoriesResponse(
                            id = jsonResponse.getInt("id"),
                            name = jsonResponse.getString("name")
                        )
                        contentList.add(contentResponse)
                        Log.d("Got Category !!", "$contentResponse")
                    }

                    return@withContext Result.Success(contentList)
                } else {
                    Log.e("DiscoverServiceRepository", "Failed to get: $responseCode")
                    return@withContext Result.Error(Exception("Failed to get categories: $responseCode"))
                }
            } catch (e: Exception) {
                Log.e("DiscoverServiceRepository", "Error getting categories: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun addContent(
        contentCategoryId: Int,
        title: String,
        summary: String,
        description: String,
        picture: String
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/content")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Discover Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonObject = JSONObject().apply {
                    put("content_category_id", contentCategoryId)
                    put("title", title)
                    put("summary", summary)
                    put("description", description)
                    put("picture", picture)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return@withContext "Content Added"
                } else {
                    Log.e("DiscoverServiceRepository", "Failed to add content: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("DiscoverServiceRepository", "Error adding content: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun editContent(
        id: Int,
        contentCategoryId: Int,
        title: String,
        summary: String,
        description: String,
        picture: String
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/content/$id")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "PUT"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Discover Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonObject = JSONObject().apply {
                    put("content_category_id", contentCategoryId)
                    put("title", title)
                    put("summary", summary)
                    put("description", description)
                    put("picture", picture)
                }

                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(jsonObject.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return@withContext "Content Edited"
                } else {
                    Log.e("DiscoverServiceRepository", "Failed to edit content: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("DiscoverServiceRepository", "Error editing content: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }

    suspend fun deleteContent(
        id: Int
    ): Any? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("$baseUrl/content/$id")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "DELETE"

                val token = tokenDataStore.getToken.first()
                if (token != null) {
                    connection.setRequestProperty("Authorization", token)
                    Log.d("Token Here", token)
                } else {
                    Log.e("Discover Service Repository", "Token is null")
                    return@withContext Result.Error(Exception("Token is null"))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return@withContext "Content Deleted"
                } else {
                    Log.e("DiscoverServiceRepository", "Failed to delete content: $responseCode")
                    return@withContext null
                }

            } catch (e: Exception) {
                Log.e("DiscoverServiceRepository", "Error deleting content: ${e.localizedMessage}", e)
                return@withContext Result.Error(e)
            }
        }
    }
}
