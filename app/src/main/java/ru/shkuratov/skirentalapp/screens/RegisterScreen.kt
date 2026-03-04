package ru.shkuratov.skirentalapp.screens

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun RegisterScreen(
    onRegistrationSuccess: (String) -> Unit,
    store: AuthStore,
) {
    var registerPhone by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val state by store.state.collectAsState()

    // Синхронизируем локальное состояние с состоянием store при необходимости
    LaunchedEffect(state.error) {
        error = state.error
    }

    LaunchedEffect(store.effects) {
        store.effects.collectLatest { effect ->
            when (effect) {
                is AuthEffect.NavigateToMain -> onRegistrationSuccess(effect.phone)
                is AuthEffect.ShowError -> {
                    error = effect.message
                }

                else -> {}
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
            text = "Регистрация",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = 0.0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            trackColor = MaterialTheme.colorScheme.surface,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Создайте аккаунт для аренды снаряжения",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = registerPhone,
            onValueChange = { registerPhone = it },
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
            value = registerPassword,
            onValueChange = { registerPassword = it },
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Повторите пароль") },
            placeholder = {
                Text(
                    "Повторите пароль", color = Color(0xFF999999)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (state.isLoading) "Регистрация..." else "Зарегистрироваться",
            enabled = registerPhone.isNotBlank() && registerPassword.isNotBlank() && confirmPassword.isNotBlank() && registerPassword == confirmPassword && !state.isLoading,
            onClick = {
                // Обновляем состояние store перед отправкой интента
                store.setState { it.copy(phone = registerPhone, password = registerPassword) }
                store.handleIntent(AuthIntent.RegisterClicked) // Используем тот же интент для упрощения
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* Здесь можно добавить переход на экран авторизации */ }) {
            Text(
                text = "Уже есть аккаунт? Войти",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }

        error?.let { errorMessage ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Создавая аккаунт, вы соглашаетесь с условиями использования",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

