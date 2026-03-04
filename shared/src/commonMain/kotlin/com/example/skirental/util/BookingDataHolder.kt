package com.example.skirental.util

import com.example.skirental.model.Booking
import com.example.skirental.model.EquipmentType
import com.example.skirental.model.Gender

/**
 * Простой кросс-платформенный holder для накопления данных между экранами.
 * В реальном проекте можно заменить на DI/Repository или сохранение в БД.
 */
data class BookingDraft(
    val equipmentType: EquipmentType? = null,
    val gender: Gender? = null,
    val age: Int? = null,
    val heightCm: Int? = null,
    val weightKg: Int? = null,
    val shoeSize: Int? = null,
    val hatSizeCm: Int? = null,
    val fullName: String = "",
    val phone: String = "",
    val date: String = "",
    val timeSlot: String = "",
) {
    fun toBookingRequest(): Booking? {
        val equipment = equipmentType ?: return null
        val genderVal = gender ?: return null
        val ageVal = age ?: return null
        val heightVal = heightCm ?: return null
        val weightVal = weightKg ?: return null
        val shoeVal = shoeSize ?: return null
        val hatVal = hatSizeCm ?: return null
        if (fullName.isBlank() || phone.isBlank() || date.isBlank() || timeSlot.isBlank()) return null

        return Booking(
            equipmentType = when (equipment) {
                EquipmentType.SKIS -> "skis"
                EquipmentType.SNOWBOARD -> "snowboard"
            },
            gender = when (genderVal) {
                Gender.MALE -> "male"
                Gender.FEMALE -> "female"
            },
            age = ageVal,
            heightCm = heightVal,
            weightKg = weightVal,
            shoeSize = shoeVal,
            hatSizeCm = hatVal,
            fullName = fullName,
            phone = phone,
            date = date,
            timeSlot = timeSlot
        )
    }
}

object BookingDraftHolder {
    var draft: BookingDraft = BookingDraft()
}

