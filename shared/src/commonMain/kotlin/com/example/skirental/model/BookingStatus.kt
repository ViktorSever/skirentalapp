package com.example.skirental.model

import kotlinx.serialization.Serializable

@Serializable
enum class BookingStatus {
    NEW, CONFIRMED, COMPLETED, CANCELLED
}
