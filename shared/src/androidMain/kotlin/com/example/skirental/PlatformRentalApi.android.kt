package com.example.skirental.util

import com.example.skirental.ApiClient
import com.example.skirental.api.RentalApi

actual fun platformRentalApi(): RentalApi = ApiClient().rentalApi

