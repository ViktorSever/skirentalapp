package com.example.skirental.util

import com.example.skirental.IosRentalApi
import com.example.skirental.api.RentalApi

actual fun platformRentalApi(): RentalApi = IosRentalApi()

