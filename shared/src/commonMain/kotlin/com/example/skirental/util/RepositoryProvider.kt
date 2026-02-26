package com.example.skirental.util

import com.example.skirental.api.RentalApi
import com.example.skirental.repository.RentalRepository
import com.example.skirental.repository.RentalRepositoryImpl

expect fun platformRentalApi(): RentalApi

fun provideRentalRepository(): RentalRepository = RentalRepositoryImpl(platformRentalApi())

