package com.example.skirental.state

import com.example.skirental.model.Gender

data class BookingSettingsState(
    val gender: Gender? = null,
    val age: Int = 0,
    val heightCm: Int = 0,
    val weightKg: Int = 0,
    val shoeSize: Int = 0,
    val hatSizeCm: Int = 0,
    val hasChanges: Boolean = false,
) {
    val isNextEnabled: Boolean
        get() = gender != null && age > 0 && heightCm > 0 && weightKg > 0 && shoeSize > 0 && hatSizeCm > 0
}
