package com.example.skirental.state

import com.example.skirental.model.Gender

data class BookingSettingsState(
    val gender: Gender? = null,
    val age: Int = DEFAULT_AGE,
    val heightCm: Int = DEFAULT_HEIGHT_CM,
    val weightKg: Int = DEFAULT_WEIGHT_KG,
    val shoeSize: Int = DEFAULT_SHOE_SIZE,
    val hatSizeCm: Int = DEFAULT_HAT_SIZE_CM,
    val hasChanges: Boolean = false
) {
    val isNextEnabled: Boolean
        get() = hasChanges
}

const val DEFAULT_AGE = 25
const val DEFAULT_HEIGHT_CM = 170
const val DEFAULT_WEIGHT_KG = 75
const val DEFAULT_SHOE_SIZE = 42
const val DEFAULT_HAT_SIZE_CM = 58

