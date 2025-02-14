package com.example.fullhealthcareapplication.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenDataStore (private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("SessionToken")
        val SESSION_TOKEN_KEY = stringPreferencesKey("session_token")
        val USER_NRIC_KEY = stringPreferencesKey("user_nric")
        val USER_ROLE_KEY = stringPreferencesKey("user_role")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email") // THIS ONE CAN BE MODIFIED
        val USER_FULL_NAME_KEY = stringPreferencesKey("user_full_name") // THIS ONE CAN BE MODIFIED
        val USER_ID =
            intPreferencesKey("user_id") // TAKEN FROM HEALTH SERVICE UPON LOGIN
    }

    val getToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SESSION_TOKEN_KEY] ?: ""
        }

    val getNric: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NRIC_KEY] ?: ""
        }

    val getRole: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ROLE_KEY] ?: ""
        }

    val getEmail: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL_KEY] ?: ""
        }

    val getFullName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_FULL_NAME_KEY] ?: ""
        }

    val getId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID] ?: 0
        }

    suspend fun saveToken(name: String) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_TOKEN_KEY] = name
        }
    }

    suspend fun saveNric(nric: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NRIC_KEY] = nric
        }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role
        }
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }

    suspend fun saveFullName(fullName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_FULL_NAME_KEY] = fullName
        }
    }

    suspend fun saveId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = id
        }
    }

    suspend fun editUserProfile(
        email: String,
        fullName: String
    ){
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_FULL_NAME_KEY] = fullName
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(SESSION_TOKEN_KEY)
            preferences.remove(USER_NRIC_KEY)
            preferences.remove(USER_ROLE_KEY)
            preferences.remove(USER_EMAIL_KEY)
            preferences.remove(USER_FULL_NAME_KEY)
            preferences.remove(USER_ID)
        }
    }
}