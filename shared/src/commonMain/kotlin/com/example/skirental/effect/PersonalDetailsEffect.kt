package com.example.skirental.effect

sealed class PersonalDetailsEffect {
    object NavigateToBookings : PersonalDetailsEffect() // ✅ Переход на "Мои брони"
    object ShowBookingSuccess : PersonalDetailsEffect()
    data class ShowError(val message: String) : PersonalDetailsEffect()
}

