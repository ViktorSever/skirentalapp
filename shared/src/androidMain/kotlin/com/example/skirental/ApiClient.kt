package com.example.skirental

import com.example.skirental.api.RentalApi
import com.example.skirental.model.BookingRequest
import com.example.skirental.model.BookingResponse
import com.example.skirental.model.EquipmentResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private interface RentalRetrofitService {
    @GET("equipment/available")
    suspend fun getAvailableEquipment(): List<EquipmentResponse>

    @POST("booking/create")
    suspend fun createBooking(@Body bookingRequest: BookingRequest): BookingResponse
}

class ApiClient {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.skiapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    private val service: RentalRetrofitService = retrofit.create(RentalRetrofitService::class.java)

    val rentalApi: RentalApi = object : RentalApi {
        override suspend fun getAvailableEquipment(): List<EquipmentResponse> {
            return service.getAvailableEquipment()
        }

        override suspend fun createBooking(bookingRequest: BookingRequest): BookingResponse {
            return service.createBooking(bookingRequest)
        }
    }
}

