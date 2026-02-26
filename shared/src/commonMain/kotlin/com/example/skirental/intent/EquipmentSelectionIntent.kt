package com.example.skirental.intent

import com.example.skirental.model.EquipmentType

sealed class EquipmentSelectionIntent {
    data class SelectEquipment(val type: EquipmentType) : EquipmentSelectionIntent()
    object NextClicked : EquipmentSelectionIntent()
}

