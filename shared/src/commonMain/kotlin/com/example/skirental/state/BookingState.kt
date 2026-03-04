package com.example.skirental.state

import com.example.skirental.model.Booking


data class BookingState(
    val bookings: List<Booking> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)