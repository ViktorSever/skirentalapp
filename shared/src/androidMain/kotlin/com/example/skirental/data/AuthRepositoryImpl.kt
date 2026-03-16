package com.example.skirental.data

import com.example.skirental.interfaces.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    override suspend fun login(phone: String, password: String): Result<String> {
        return try {
            val query =
                usersCollection.whereEqualTo("phone", phone).whereEqualTo("password", password)
                    .get().await()

            if (!query.isEmpty) {
                Result.success(phone)
            } else {
                Result.failure(Exception("Неверный номер телефона или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(phone: String, password: String): Result<String> {
        return try {
            usersCollection.document(phone).set(
                hashMapOf(
                    "phone" to phone, "password" to password
                )
            ).await()
            Result.success(phone)
        } catch (e: Exception) {
            if (e.message?.contains("already exists") == true) {
                Result.failure(Exception("Пользователь с таким номером уже существует"))
            } else {
                Result.failure(e)
            }
        }
    }
}