package com.example.skirental

interface UserPreferences {
    suspend fun saveUserToken(token: String)
    suspend fun getUserToken(): String?
    suspend fun clearUserToken()
}