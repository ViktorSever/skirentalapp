package com.example.skirental

import com.example.skirental.effect.PersonalDetailsEffect
import com.example.skirental.intent.PersonalDetailsIntent
import com.example.skirental.repository.RentalRepository
import com.example.skirental.state.PersonalDetailsState
import com.example.skirental.util.BookingDraftHolder
import com.example.skirental.util.MviStore
import com.example.skirental.util.provideRentalRepository

class PersonalDetailsKmpViewModel(
    private val repository: RentalRepository = provideRentalRepository()
) : MviStore<PersonalDetailsIntent, PersonalDetailsState, PersonalDetailsEffect>(
    initialState = PersonalDetailsState()
) {

    override suspend fun reduce(intent: PersonalDetailsIntent) {
        when (intent) {
            is PersonalDetailsIntent.FullNameChanged -> setState { it.copy(fullName = intent.value) }
            is PersonalDetailsIntent.PhoneChanged -> setState { it.copy(phone = intent.value) }
            is PersonalDetailsIntent.DateChanged -> setState { it.copy(date = intent.value) }
            is PersonalDetailsIntent.TimeSlotChanged -> setState { it.copy(timeSlot = intent.value) }
            PersonalDetailsIntent.ConfirmClicked -> {
                val s = state.value
                if (!s.isConfirmEnabled) {
                    emitEffect(PersonalDetailsEffect.ShowError("Please fill all fields"))
                    return
                }

                BookingDraftHolder.draft = BookingDraftHolder.draft.copy(
                    fullName = s.fullName,
                    phone = s.phone,
                    date = s.date,
                    timeSlot = s.timeSlot
                )

                val request = BookingDraftHolder.draft.toBookingRequest()
                if (request == null) {
                    emitEffect(PersonalDetailsEffect.ShowError("Booking data incomplete"))
                    return
                }

                try {
                    val response = repository.createBooking(request)
                    if (response.status.equals("success", ignoreCase = true)) {
                        emitEffect(PersonalDetailsEffect.ShowBookingSuccess)
                    } else {
                        emitEffect(PersonalDetailsEffect.ShowError("Booking failed: ${response.status}"))
                    }
                } catch (e: Exception) {
                    emitEffect(PersonalDetailsEffect.ShowError("Network error: ${e.message.orEmpty()}"))
                }
            }
        }
    }
}

