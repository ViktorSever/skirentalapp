package ru.shkuratov.skirentalapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skirental.intent.PersonalDetailsIntent
import com.example.skirental.state.PersonalDetailsState
import kotlinx.coroutines.launch
import ru.shkuratov.skirentalapp.screens.BookingTopBar
import ru.shkuratov.skirentalapp.screens.PrimaryButton
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetailsScreen(
    state: PersonalDetailsState,
    onIntent: (PersonalDetailsIntent) -> Unit,
    onBack: () -> Unit,
) {
    val showTimeSheet = remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BookingTopBar(
                title = "Ваши данные", onBackClick = onBack
            )

            LinearProgressIndicator(
                progress = 1f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                trackColor = MaterialTheme.colorScheme.surface,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.fullName,
                onValueChange = { onIntent(PersonalDetailsIntent.FullNameChanged(it)) },
                label = { Text("ФИО") },
                placeholder = { Text("Введите ФИО", color = Color(0xFF999999)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.phone,
                onValueChange = { },
                label = { Text("Телефон") },
                placeholder = {
                    Text(
                        if (state.phone.isBlank()) "Из профиля" else "", color = Color(0xFF999999)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "График аренды", style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            DateField(
                label = "Дата получения",
                value = state.date,
                onClick = { showDatePicker.value = true })

            Spacer(modifier = Modifier.height(8.dp))

            DateField(
                label = "Время получения",
                value = state.timeSlot,
                onClick = { showTimeSheet.value = true })

            Spacer(modifier = Modifier.height(16.dp))

            PickupLocationCard()

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "Подтвердить бронирование",
                enabled = state.isConfirmEnabled,
                onClick = { onIntent(PersonalDetailsIntent.ConfirmClicked) },
            )
        }
    }

    if (showTimeSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showTimeSheet.value = false }) {
            TimeSlotSheet(
                onSelect = { slot ->
                    onIntent(PersonalDetailsIntent.TimeSlotChanged(slot))
                    coroutineScope.launch {
                        showTimeSheet.value = false
                    }
                })
        }
    }

    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(onDismissRequest = { showDatePicker.value = false }, confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        val formatted = localDate.toString()
                        onIntent(PersonalDetailsIntent.DateChanged(formatted))
                    }
                    showDatePicker.value = false
                }) {
                Text("ОК")
            }
        }, dismissButton = {
            TextButton(onClick = { showDatePicker.value = false }) {
                Text("Отмена")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun DateField(
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label, style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (value.isBlank()) "Выберите $label" else value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            fontSize = 16.sp
        )
    }
}

@Composable
private fun PickupLocationCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Место получения", style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "База Summit, Стойка №4", style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Показать на карте", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp
        )
    }
}

@Composable
private fun TimeSlotSheet(
    onSelect: (String) -> Unit,
) {
    val slots = listOf(
        "09:00–10:30", "10:30–12:00", "12:00–13:30", "13:30–15:00", "15:00–16:30"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Выберите время",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        slots.forEach { slot ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(slot) }
                    .padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = slot, modifier = Modifier.weight(1f))
            }
        }
    }
}
