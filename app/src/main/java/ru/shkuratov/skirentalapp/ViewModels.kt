package ru.shkuratov.skirentalapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skirental.BookingSettingsKmpViewModel
import com.example.skirental.EquipmentSelectionKmpViewModel
import com.example.skirental.PersonalDetailsKmpViewModel
import com.example.skirental.effect.BookingSettingsEffect
import com.example.skirental.effect.EquipmentSelectionEffect
import com.example.skirental.effect.PersonalDetailsEffect
import com.example.skirental.intent.BookingSettingsIntent
import com.example.skirental.intent.EquipmentSelectionIntent
import com.example.skirental.intent.PersonalDetailsIntent
import com.example.skirental.state.BookingSettingsState
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

class BookingSettingsViewModel : ViewModel() {
    private val kmpViewModel = BookingSettingsKmpViewModel()

    val state: StateFlow<BookingSettingsState> = kmpViewModel.state
    val effects: SharedFlow<BookingSettingsEffect> = kmpViewModel.effects

    fun handleIntent(intent: BookingSettingsIntent) {
        kmpViewModel.handleIntent(intent)
    }

    override fun onCleared() {
        super.onCleared()
        kmpViewModel.clear()
    }
}

class PersonalDetailsViewModel : ViewModel() {
    private val kmpViewModel = PersonalDetailsKmpViewModel()

    val state: StateFlow<PersonalDetailsState> = kmpViewModel.state
    val effects: SharedFlow<PersonalDetailsEffect> = kmpViewModel.effects

    fun handleIntent(intent: PersonalDetailsIntent) {
        kmpViewModel.handleIntent(intent)
    }

    override fun onCleared() {
        super.onCleared()
        kmpViewModel.clear()
    }
}

