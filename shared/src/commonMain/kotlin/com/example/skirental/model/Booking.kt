package com.example.skirental.model

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val id: String = "",
    val equipmentType: String = "",
    val gender: String = "",
    val age: Int = 0,
    val heightCm: Int = 0,
    val weightKg: Int = 0,
    val shoeSize: Int = 0,
    val hatSizeCm: Int = 0,
    val fullName: String = "",
    val phone: String = "",
    val date: String = "",
    val timeSlot: String = "",
    val status: String = "NEW", // String вместо enum для Firestore
) {
    fun getStatusEnum(): BookingStatus = when (status) {
        "CONFIRMED" -> BookingStatus.CONFIRMED
        "COMPLETED" -> BookingStatus.COMPLETED
        "CANCELLED" -> BookingStatus.CANCELLED
        else -> BookingStatus.NEW
    }
}