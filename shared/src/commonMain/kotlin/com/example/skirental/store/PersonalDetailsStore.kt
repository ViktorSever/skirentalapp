package com.example.skirental.store

import com.example.skirental.UserPreferences
import com.example.skirental.effect.PersonalDetailsEffect
import com.example.skirental.intent.PersonalDetailsIntent
import com.example.skirental.interfaces.RentalRepository
import com.example.skirental.state.PersonalDetailsState
import com.example.skirental.util.BookingDraftHolder
import com.example.skirental.util.MviStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonalDetailsStore(
    scope: CoroutineScope,
    private val repository: RentalRepository,
    private val userPreferences: UserPreferences, // ✅ SharedPreferences
    initialState: PersonalDetailsState = PersonalDetailsState(),
) : MviStore<PersonalDetailsIntent, PersonalDetailsState, PersonalDetailsEffect>(initialState) {

    init {
        scope.launch(Dispatchers.Main) {
            val phone = userPreferences.getUserToken() ?: ""
            setState { it.copy(phone = phone) }
        }
    }

    override suspend fun reduce(intent: PersonalDetailsIntent) {
        when (intent) {
            is PersonalDetailsIntent.FullNameChanged -> {
                setState { it.copy(fullName = intent.value) }
            }

            is PersonalDetailsIntent.PhoneChanged -> {
                // ✅ Игнорируем - телефон только из токена
            }

            is PersonalDetailsIntent.DateChanged -> {
                setState { it.copy(date = intent.value) }
            }

            is PersonalDetailsIntent.TimeSlotChanged -> {
                setState { it.copy(timeSlot = intent.value) }
            }

            is PersonalDetailsIntent.ConfirmClicked -> {
                handleConfirmBooking()
            }
        }
    }

    private suspend fun handleConfirmBooking() {
        val s = state.value
        if (isFormValid(s)) {
            BookingDraftHolder.draft = BookingDraftHolder.draft.copy(
                fullName = s.fullName, phone = s.phone, date = s.date, timeSlot = s.timeSlot
            )
            val request = BookingDraftHolder.draft.toBookingRequest()
            if (request == null) {
                emitEffect(PersonalDetailsEffect.ShowError("Booking data incomplete"))
                return
            }
            repository.createBooking(request).onSuccess { _ ->
                emitEffect(PersonalDetailsEffect.ShowBookingSuccess)
                emitEffect(PersonalDetailsEffect.NavigateToBookings)
            }.onFailure { exception ->
                emitEffect(PersonalDetailsEffect.ShowError(exception.message ?: "Booking failed"))
            }
        } else {
            emitEffect(PersonalDetailsEffect.ShowError("Заполните все поля"))
        }
    }

    private fun isFormValid(state: PersonalDetailsState): Boolean {
        return state.fullName.isNotBlank() && state.phone.isNotBlank() && state.date.isNotBlank() && state.timeSlot.isNotBlank()
    }
}
