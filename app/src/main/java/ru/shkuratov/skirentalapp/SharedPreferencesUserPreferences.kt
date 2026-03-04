package ru.shkuratov.skirentalapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.skirental.UserPreferences
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class SharedPreferencesUserPreferences(
    context: Context,
) : UserPreferences {

    private val dataStore = context.dataStore

    override suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun getUserToken(): String? {
        return dataStore.data.first()[TOKEN_KEY]
    }

    override suspend fun clearUserToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("user_token")
    }
}
