package com.example.skirental.effect

sealed class PersonalDetailsEffect {
    object ShowBookingSuccess : PersonalDetailsEffect()
    data class ShowError(val message: String) : PersonalDetailsEffect()
}

