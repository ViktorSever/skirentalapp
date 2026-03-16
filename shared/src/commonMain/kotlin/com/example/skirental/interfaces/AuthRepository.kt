package com.example.skirental.interfaces

interface AuthRepository {
    suspend fun login(phone: String, password: String): Result<String>
    suspend fun register(phone: String, password: String): Result<String>
}