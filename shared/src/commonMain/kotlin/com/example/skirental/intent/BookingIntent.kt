package com.example.skirental.intent

sealed class BookingIntent {
    object LoadBookings : BookingIntent()
    object RefreshBookings : BookingIntent()
    data class CancelBooking(val bookingId: String) : BookingIntent()
    data class ReturnBooking(val bookingId: String) : BookingIntent()
}
