package com.example.skirental.interfaces

import com.example.skirental.model.Booking
import com.example.skirental.model.Equipment

interface RentalRepository {
    suspend fun requestEquipment(): Result<List<Equipment>>
    suspend fun createBooking(request: Booking): Result<String>
}