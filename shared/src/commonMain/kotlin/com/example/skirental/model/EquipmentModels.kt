package com.example.skirental.model

import kotlinx.serialization.Serializable

enum class EquipmentType {
    SKIS,
    SNOWBOARD
}

enum class Gender {
    MALE,
    FEMALE
}

@Serializable
data class EquipmentResponse(
    val id: String,
    val type: String, // "skis" or "snowboard"
    val available: Boolean
)

@Serializable
data class BookingRequest(
    val equipmentType: String,
    val gender: String,
    val age: Int,
    val heightCm: Int,
    val weightKg: Int,
    val shoeSize: Int,
    val hatSizeCm: Int,
    val fullName: String,
    val phone: String,
    val date: String,
    val timeSlot: String
)

@Serializable
data class BookingResponse(
    val bookingId: String,
    val status: String
)

