package com.example.skirental.repository

import com.example.skirental.api.RentalApi
import com.example.skirental.model.BookingRequest
import com.example.skirental.model.BookingResponse
import com.example.skirental.model.EquipmentResponse

interface RentalRepository {
    suspend fun getAvailableEquipment(): List<EquipmentResponse>
    suspend fun createBooking(request: BookingRequest): BookingResponse
}

class RentalRepositoryImpl(
    private val api: RentalApi
) : RentalRepository {

    override suspend fun getAvailableEquipment(): List<EquipmentResponse> {
        return api.getAvailableEquipment()
    }

    override suspend fun createBooking(request: BookingRequest): BookingResponse {
        return api.createBooking(request)
    }
}

