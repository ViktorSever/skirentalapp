package com.example.skirental.store

import com.example.skirental.UserPreferences
import com.example.skirental.effect.BookingEffect
import com.example.skirental.intent.BookingIntent
import com.example.skirental.interfaces.BookingRepository
import com.example.skirental.model.BookingStatus
import com.example.skirental.state.BookingState
import com.example.skirental.util.MviStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingStore(
    scope: CoroutineScope,
    private val userPreferences: UserPreferences,
    private val bookingRepository: BookingRepository,
    initialState: BookingState = BookingState(),
) : MviStore<BookingIntent, BookingState, BookingEffect>(initialState) {

    init {
        scope.launch(Dispatchers.Main) {
            loadBookings()
        }
    }

    public override suspend fun reduce(intent: BookingIntent) {
        when (intent) {
            is BookingIntent.LoadBookings,
            is BookingIntent.RefreshBookings,
                -> loadBookings()

            is BookingIntent.CancelBooking -> updateStatus(
                intent.bookingId, BookingStatus.CANCELLED
            )

            is BookingIntent.ReturnBooking -> updateStatus(
                intent.bookingId, BookingStatus.COMPLETED
            )
        }
    }

    private suspend fun loadBookings() {
        setState { it.copy(isLoading = true, error = null) }
        try {
            val phone = userPreferences.getUserToken() ?: return
            val result = bookingRepository.getBookingsByPhone(phone)
            result.onSuccess { bookings ->
                setState { it.copy(bookings = bookings, isLoading = false) }
            }.onFailure { error ->
                setState { it.copy(error = error.message ?: "Ошибка загрузки", isLoading = false) }
            }
        } catch (e: Exception) {
            setState { it.copy(error = e.message ?: "Ошибка сети", isLoading = false) }
        }
    }

    private suspend fun updateStatus(bookingId: String, status: BookingStatus) {
        setState { it.copy(isLoading = true) }
        try {
            val result = bookingRepository.updateBookingStatus(bookingId, status)
            result.onSuccess {
                loadBookings()
            }.onFailure { error ->
                setState {
                    it.copy(
                        error = "Ошибка обновления: ${error.message}", isLoading = false
                    )
                }
            }
        } catch (e: Exception) {
            setState { it.copy(error = e.message ?: "Ошибка сети", isLoading = false) }
        }
    }
}
