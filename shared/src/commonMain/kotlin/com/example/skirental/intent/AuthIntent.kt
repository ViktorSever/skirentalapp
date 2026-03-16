package com.example.skirental.intent

sealed class AuthIntent {
    data class PhoneChanged(val phone: String) : AuthIntent()
    data class PasswordChanged(val password: String) : AuthIntent()
    object LoginClicked : AuthIntent()
    object OpenRegisterScreen : AuthIntent() // Новый интент для регистрации

    object RegisterClicked : AuthIntent()


}