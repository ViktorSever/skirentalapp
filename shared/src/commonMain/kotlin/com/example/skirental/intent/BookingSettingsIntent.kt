package com.example.skirental.intent

import com.example.skirental.model.Gender

sealed class BookingSettingsIntent {
    data class GenderSelected(val gender: Gender) : BookingSettingsIntent()
    data class AgeChanged(val value: Int) : BookingSettingsIntent()
    data class HeightChanged(val value: Int) : BookingSettingsIntent()
    data class WeightChanged(val value: Int) : BookingSettingsIntent()
    data class ShoeSizeChanged(val value: Int) : BookingSettingsIntent()
    data class HatSizeChanged(val value: Int) : BookingSettingsIntent()
    object NextClicked : BookingSettingsIntent()
}

