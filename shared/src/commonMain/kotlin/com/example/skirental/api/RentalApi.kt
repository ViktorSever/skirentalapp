package com.example.skirental.api

import com.example.skirental.model.BookingRequest
import com.example.skirental.model.BookingResponse
import com.example.skirental.model.EquipmentResponse

interface RentalApi {
    suspend fun getAvailableEquipment(): List<EquipmentResponse>
    suspend fun createBooking(bookingRequest: BookingRequest): BookingResponse
}

