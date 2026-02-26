package com.example.skirental.effect

sealed class EquipmentSelectionEffect {
    object NavigateToBookingSettings : EquipmentSelectionEffect()
    data class ShowError(val message: String) : EquipmentSelectionEffect()
}

