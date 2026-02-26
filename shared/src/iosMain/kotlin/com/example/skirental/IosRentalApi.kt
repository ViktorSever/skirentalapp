package com.example.skirental

import com.example.skirental.api.RentalApi
import com.example.skirental.model.BookingRequest
import com.example.skirental.model.BookingResponse
import com.example.skirental.model.EquipmentResponse

/**
 * Простая заглушка для iOS-платформы.
 * В дальнейшем можно заменить на реальный Ktor-клиент.
 */
class IosRentalApi : RentalApi {
    override suspend fun getAvailableEquipment(): List<EquipmentResponse> {
        return emptyList()
    }

    override suspend fun createBooking(bookingRequest: BookingRequest): BookingResponse {
        return BookingResponse(
            bookingId = "local-stub",
            status = "success"
        )
    }
}

