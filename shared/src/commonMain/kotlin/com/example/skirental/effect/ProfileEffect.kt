package com.example.skirental.effect

sealed class ProfileEffect {
    object NavigateToBookings : ProfileEffect()
    object LogoutSuccess : ProfileEffect()
}