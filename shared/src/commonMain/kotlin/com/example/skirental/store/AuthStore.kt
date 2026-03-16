package com.example.skirental.store

import com.example.skirental.UserPreferences
import com.example.skirental.effect.AuthEffect
import com.example.skirental.intent.AuthIntent
import com.example.skirental.interfaces.AuthRepository
import com.example.skirental.state.AuthState
import com.example.skirental.util.MviStore

class AuthStore(
    private val repository: AuthRepository,
    private val userPreferences: UserPreferences,
    initialState: AuthState = AuthState(),
) : MviStore<AuthIntent, AuthState, AuthEffect>(initialState) {

    override suspend fun reduce(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.PhoneChanged -> setState { it.copy(phone = intent.phone, error = null) }
            is AuthIntent.PasswordChanged -> setState {
                it.copy(
                    password = intent.password, error = null
                )
            }

            AuthIntent.LoginClicked -> login()
            AuthIntent.OpenRegisterScreen -> {
                setState { it.copy(showRegistration = true) }
                emitEffect(AuthEffect.NavigateToRegister)
            }

            AuthIntent.RegisterClicked -> {
                register()
            }
        }
    }

    private suspend fun register() {
        setState { it.copy(isLoading = true, error = null) }
        try {
            val currentState = state.value
            val result = repository.register(currentState.phone, currentState.password)

            result.onSuccess { phone ->
                savePhoneToPreferences(phone)
                setState { it.copy(isLoading = false) }
                emitEffect(AuthEffect.NavigateToMain(phone))

            }.onFailure { exception ->
                setState {
                    it.copy(
                        isLoading = false, error = exception.message ?: "Ошибка регистрации"
                    )
                }
            }
        } catch (e: Exception) {
            setState {
                it.copy(
                    isLoading = false, error = e.message ?: "Ошибка сети"
                )
            }
        } finally {
            setState { it.copy(isLoading = false) }
        }
    }

    private suspend fun login() {
        setState { it.copy(isLoading = true, error = null) }

        try {
            val currentState = state.value
            val result = repository.login(currentState.phone, currentState.password)

            result.onSuccess { phone ->
                savePhoneToPreferences(phone)
                setState { it.copy(isLoading = false) }
                emitEffect(AuthEffect.NavigateToMain(phone))
            }.onFailure { exception ->
                setState {
                    it.copy(
                        isLoading = false, error = exception.message ?: "Ошибка авторизации"
                    )
                }
            }
        } catch (e: Exception) {
            setState {
                it.copy(
                    isLoading = false, error = e.message ?: "Ошибка авторизации"
                )
            }
        } finally {
            // Ничего не делаем — состояние уже обновлено
        }
    }

    private suspend fun savePhoneToPreferences(phone: String) {
        userPreferences.saveUserToken(phone)
    }

}