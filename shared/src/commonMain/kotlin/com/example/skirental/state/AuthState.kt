package com.example.skirental.state

data class AuthState(
    val phone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val showRegistration: Boolean = false,
)

