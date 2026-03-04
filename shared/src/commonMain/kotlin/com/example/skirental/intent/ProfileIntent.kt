package com.example.skirental.intent

sealed class ProfileIntent {
    object LogoutClicked : ProfileIntent()
    object OpenBookingsClicked : ProfileIntent()
}