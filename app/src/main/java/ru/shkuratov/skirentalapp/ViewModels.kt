package ru.shkuratov.skirentalapp

import androidx.lifecycle.ViewModel
import com.example.skirental.EquipmentSelectionKmpViewModel
import com.example.skirental.data.RentalRepositoryImpl
import com.example.skirental.effect.EquipmentSelectionEffect
import com.example.skirental.effect.PersonalDetailsEffect
import com.example.skirental.intent.EquipmentSelectionIntent
import com.example.skirental.intent.PersonalDetailsIntent
import com.example.skirental.state.EquipmentSelectionState
import com.example.skirental.state.PersonalDetailsState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class EquipmentSelectionViewModel : ViewModel() {
    private val kmpViewModel = EquipmentSelectionKmpViewModel()

    val state: StateFlow<EquipmentSelectionState> = kmpViewModel.state
    val effects: SharedFlow<EquipmentSelectionEffect> = kmpViewModel.effects

    fun handleIntent(intent: EquipmentSelectionIntent) {
        kmpViewModel.handleIntent(intent)
    }

    override fun onCleared() {
        super.onCleared()
        kmpViewModel.clear()
    }
}

