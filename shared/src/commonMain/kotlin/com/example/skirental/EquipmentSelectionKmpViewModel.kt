package com.example.skirental

import com.example.skirental.effect.EquipmentSelectionEffect
import com.example.skirental.intent.EquipmentSelectionIntent
import com.example.skirental.model.EquipmentType
import com.example.skirental.state.EquipmentSelectionState
import com.example.skirental.util.BookingDraftHolder
import com.example.skirental.util.BookingDraft
import com.example.skirental.util.MviStore

class EquipmentSelectionKmpViewModel :
    MviStore<EquipmentSelectionIntent, EquipmentSelectionState, EquipmentSelectionEffect>(
        initialState = EquipmentSelectionState.Content()
    ) {

    override suspend fun reduce(intent: EquipmentSelectionIntent) {
        when (intent) {
            is EquipmentSelectionIntent.SelectEquipment -> {
                setState {
                    (it as? EquipmentSelectionState.Content)?.copy(
                        selectedEquipment = intent.type, isNextEnabled = true
                    ) ?: it
                }
            }

            EquipmentSelectionIntent.NextClicked -> {
                val current = state.value
                if (current is EquipmentSelectionState.Content && current.selectedEquipment != null) {
                    BookingDraftHolder.draft = BookingDraft(
                        equipmentType = current.selectedEquipment
                    )
                    emitEffect(EquipmentSelectionEffect.NavigateToBookingSettings)
                } else {
                    emitEffect(EquipmentSelectionEffect.ShowError("Please select equipment"))
                }
            }
        }
    }
}

