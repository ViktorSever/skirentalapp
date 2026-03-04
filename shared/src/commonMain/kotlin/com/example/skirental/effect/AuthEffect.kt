package com.example.skirental.effect

// AuthEffect.kt
sealed class AuthEffect {
    data class NavigateToMain(val phone: String) : AuthEffect()
    data object NavigateToRegister : AuthEffect()
    data class ShowError(val message: String) : AuthEffect()
}