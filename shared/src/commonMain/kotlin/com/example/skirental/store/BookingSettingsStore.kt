package com.example.skirental.store


import com.example.skirental.effect.BookingSettingsEffect
import com.example.skirental.intent.BookingSettingsIntent
import com.example.skirental.model.Gender
import com.example.skirental.state.BookingSettingsState
import com.example.skirental.util.BookingDraft
import com.example.skirental.util.BookingDraftHolder
import com.example.skirental.util.MviStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingSettingsStore(
    scope: CoroutineScope,
    initialState: BookingSettingsState = BookingSettingsState(),
) : MviStore<BookingSettingsIntent, BookingSettingsState, BookingSettingsEffect>(initialState) {

    init {
        scope.launch(Dispatchers.Main) {
            // Инициализация пустого состояния
        }
    }

    public override suspend fun reduce(intent: BookingSettingsIntent) {
        when (intent) {
            is BookingSettingsIntent.GenderSelected -> handleGenderSelected(intent.gender)
            is BookingSettingsIntent.AgeChanged -> handleAgeChanged(intent.value)
            is BookingSettingsIntent.HeightChanged -> handleHeightChanged(intent.value)
            is BookingSettingsIntent.WeightChanged -> handleWeightChanged(intent.value)
            is BookingSettingsIntent.ShoeSizeChanged -> handleShoeSizeChanged(intent.value)
            is BookingSettingsIntent.HatSizeChanged -> handleHatSizeChanged(intent.value)
            is BookingSettingsIntent.NextClicked -> handleNextClicked()
        }
    }

    private suspend fun handleGenderSelected(gender: Gender) {
        setState {
            it.copy(gender = gender)
        }
    }

    private suspend fun handleAgeChanged(age: Int) {
        setState {
            it.copy(age = age)
        }
    }

    private suspend fun handleHeightChanged(heightCm: Int) {
        setState {
            it.copy(heightCm = heightCm)
        }
    }

    private fun handleWeightChanged(weightKg: Int) {
        setState {
            it.copy(weightKg = weightKg)
        }
    }

    private fun handleShoeSizeChanged(shoeSize: Int) {
        setState {
            it.copy(shoeSize = shoeSize)
        }
    }

    private fun handleHatSizeChanged(hatSizeCm: Int) {
        setState {
            it.copy(hatSizeCm = hatSizeCm)
        }
    }

    private fun handleNextClicked() {
        val currentState = state.value
        if (isFormValid(currentState)) {
            BookingDraftHolder.draft = BookingDraft(
                equipmentType = BookingDraftHolder.draft.equipmentType,
                gender = currentState.gender,
                age = currentState.age,
                heightCm = currentState.heightCm,
                weightKg = currentState.weightKg,
                shoeSize = currentState.shoeSize,
                hatSizeCm = currentState.hatSizeCm
            )
            emitEffect(BookingSettingsEffect.NavigateToPersonalDetails)
        } else {
            emitEffect(BookingSettingsEffect.ShowError("Заполните все поля"))
        }
    }

    private fun isFormValid(state: BookingSettingsState): Boolean {
        return state.gender != null && state.age > 0 && state.heightCm > 0 && state.weightKg > 0 && state.shoeSize > 0 && state.hatSizeCm > 0
    }
}
