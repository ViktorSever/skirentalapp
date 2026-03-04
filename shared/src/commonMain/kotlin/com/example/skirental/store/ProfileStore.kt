package com.example.skirental.store

import com.example.skirental.UserPreferences
import com.example.skirental.effect.ProfileEffect
import com.example.skirental.intent.ProfileIntent
import com.example.skirental.state.ProfileState
import com.example.skirental.util.MviStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProfileStore(
    private val scope: CoroutineScope,
    private val userPreferences: UserPreferences,
    initialState: ProfileState = ProfileState(),
) : MviStore<ProfileIntent, ProfileState, ProfileEffect>(initialState) {

    // ✅ Создаем собственный CoroutineScope

    public override suspend fun reduce(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LogoutClicked -> logout()
            ProfileIntent.OpenBookingsClicked -> emitEffect(ProfileEffect.NavigateToBookings)
        }
    }

    // ✅ Загружаем токен при инициализации
    init {
        scope.launch {
            loadPhoneNumber()
        }
    }

    private suspend fun loadPhoneNumber() {
        val phone = userPreferences.getUserToken()
        setState {
            it.copy(phoneNumber = phone ?: "")
        }
    }

    private suspend fun logout() {
        userPreferences.clearUserToken()
        setState {
            it.copy(phoneNumber = "")
        }
        emitEffect(ProfileEffect.LogoutSuccess)
    }
}
