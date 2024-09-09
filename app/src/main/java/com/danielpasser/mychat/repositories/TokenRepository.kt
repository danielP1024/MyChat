package com.danielpasser.mychat.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

class TokenRepository (private val context: Context) {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    }
    suspend fun saveAccessToken(token: String) {
        Log.e("TEST","saveAccessToken")
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }
    suspend fun deleteAccessToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }
    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }
    }
    suspend fun saveRefreshToken(refreshToken: String) {
        Log.e("TEST","saveRefreshToken ")
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }
    suspend fun deleteRefreshToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

}