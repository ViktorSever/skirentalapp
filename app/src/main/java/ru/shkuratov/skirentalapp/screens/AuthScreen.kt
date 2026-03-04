package ru.shkuratov.skirentalapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skirental.effect.AuthEffect
import com.example.skirental.intent.AuthIntent
import com.example.skirental.store.AuthStore
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    store: AuthStore,
) {
    val state by store.state.collectAsState()
    LaunchedEffect(store.effects) {
        store.effects.collectLatest { effect ->
            when (effect) {
                is AuthEffect.NavigateToMain -> {
                    onLoginSuccess(effect.phone)
                }

                AuthEffect.NavigateToRegister -> {
                    onNavigateToRegister()
                }

                is AuthEffect.ShowError -> {

                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Text(
                text = "Авторизация",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = 0.0f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Войдите в свой аккаунт для аренды снаряжения",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = state.phone,
                onValueChange = { store.handleIntent(AuthIntent.PhoneChanged(it)) },
                label = { Text("Номер телефона") },
                placeholder = {
                    Text(
                        "Номер телефона", color = Color(0xFF999999)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { store.handleIntent(AuthIntent.PasswordChanged(it)) },
                label = { Text("Пароль") },
                placeholder = {
                    Text(
                        "Пароль", color = Color(0xFF999999)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = if (state.isLoading) "Вход..." else "Авторизация",
                enabled = state.phone.isNotBlank() && state.password.isNotBlank() && !state.isLoading,
                onClick = { store.handleIntent(AuthIntent.LoginClicked) })

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { store.handleIntent(AuthIntent.OpenRegisterScreen) }) {
                Text(
                    text = "Регистрация",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }

            state.error?.let { errorMessage ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Для доступа ко всем функциям приложения",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
    }
}
