package com.example.skirental.effect

sealed class BookingSettingsEffect {
    object NavigateToPersonalDetails : BookingSettingsEffect()
    data class ShowError(val message: String) : BookingSettingsEffect()
}

