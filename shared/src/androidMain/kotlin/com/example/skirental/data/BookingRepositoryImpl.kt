package com.example.skirental.data

import com.example.skirental.interfaces.BookingRepository
import com.example.skirental.model.Booking
import com.example.skirental.model.BookingStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookingRepositoryImpl : BookingRepository {
    private val db = FirebaseFirestore.getInstance()
    private val bookingsCollection = db.collection("booking")

    override suspend fun getBookingsByPhone(phone: String): Result<List<Booking>> {
        val snapshot = bookingsCollection.whereEqualTo("phone", phone).get().await()
        val bookings = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Booking::class.java)?.copy(id = doc.id)
        }
        return Result.success(bookings)
    }

    override suspend fun updateBookingStatus(
        bookingId: String,
        status: BookingStatus,
    ): Result<Unit> {
        return try {
            bookingsCollection.document(bookingId).update("status", status.name).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}