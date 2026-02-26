package com.example.skirental.state

import com.example.skirental.model.EquipmentType

sealed class EquipmentSelectionState {
    object Loading : EquipmentSelectionState()

    data class Content(
        val selectedEquipment: EquipmentType? = null,
        val isNextEnabled: Boolean = false
    ) : EquipmentSelectionState()
}

