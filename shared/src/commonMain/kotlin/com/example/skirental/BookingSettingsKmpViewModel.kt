package com.example.skirental

import com.example.skirental.effect.BookingSettingsEffect
import com.example.skirental.intent.BookingSettingsIntent
import com.example.skirental.model.Gender
import com.example.skirental.state.BookingSettingsState
import com.example.skirental.util.BookingDraftHolder
import com.example.skirental.util.MviStore

class BookingSettingsKmpViewModel :
    MviStore<BookingSettingsIntent, BookingSettingsState, BookingSettingsEffect>(
        initialState = BookingSettingsState()
    ) {

    override suspend fun reduce(intent: BookingSettingsIntent) {
        when (intent) {
            is BookingSettingsIntent.GenderSelected -> setState { it.copy(gender = intent.gender) }
            is BookingSettingsIntent.AgeChanged -> setState { it.copy(age = intent.value, hasChanges = true) }
            is BookingSettingsIntent.HeightChanged -> setState { it.copy(heightCm = intent.value, hasChanges = true) }
            is BookingSettingsIntent.WeightChanged -> setState { it.copy(weightKg = intent.value, hasChanges = true) }
            is BookingSettingsIntent.ShoeSizeChanged -> setState { it.copy(shoeSize = intent.value, hasChanges = true) }
            is BookingSettingsIntent.HatSizeChanged -> setState { it.copy(hatSizeCm = intent.value, hasChanges = true) }
            BookingSettingsIntent.NextClicked -> {
                val s = state.value
                if (s.isNextEnabled) {
                    BookingDraftHolder.draft = BookingDraftHolder.draft.copy(
                        gender = s.gender,
                        age = s.age,
                        heightCm = s.heightCm,
                        weightKg = s.weightKg,
                        shoeSize = s.shoeSize,
                        hatSizeCm = s.hatSizeCm
                    )
                    emitEffect(BookingSettingsEffect.NavigateToPersonalDetails)
                } else {
                    emitEffect(BookingSettingsEffect.ShowError("Please fill all fields"))
                }
            }
        }
    }
}

