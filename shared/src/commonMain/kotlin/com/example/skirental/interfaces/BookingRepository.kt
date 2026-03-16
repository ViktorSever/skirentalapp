package com.example.skirental.interfaces

import com.example.skirental.model.Booking
import com.example.skirental.model.BookingStatus

interface BookingRepository {
    suspend fun getBookingsByPhone(phone: String): Result<List<Booking>>
    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit>
}
