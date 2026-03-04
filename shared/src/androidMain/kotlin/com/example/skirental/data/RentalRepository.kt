package com.example.skirental.data

import com.example.skirental.interfaces.RentalRepository
import com.example.skirental.model.Booking
import com.example.skirental.model.Equipment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RentalRepositoryImpl : RentalRepository {
    private val db = FirebaseFirestore.getInstance()
    private val bookingsCollection = db.collection("booking")
    private val equipmentCollection = db.collection("equipment")

    override suspend fun requestEquipment(): Result<List<Equipment>> {
        return try {
            val snapshot = equipmentCollection.get().await()
            val equipmentList = snapshot.toObjects(Equipment::class.java)
            Result.success(equipmentList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createBooking(request: Booking): Result<String> {
        return try {
            val documentRef = bookingsCollection.add(request).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
