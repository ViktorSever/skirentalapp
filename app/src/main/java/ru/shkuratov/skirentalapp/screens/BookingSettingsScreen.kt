package ru.shkuratov.skirentalapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skirental.intent.BookingSettingsIntent
import com.example.skirental.model.Gender
import com.example.skirental.state.BookingSettingsState

@Composable
fun BookingSettingsScreen(
    state: BookingSettingsState,
    onIntent: (BookingSettingsIntent) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BookingTopBar(
                title = "Настройки бронирования", onBackClick = onBack
            )

            LinearProgressIndicator(
                progress = 0.66f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                trackColor = MaterialTheme.colorScheme.surface,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ ПОЛЕ ПОЛА
            Text(
                text = "Пол", style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            SegmentedToggle(
                options = listOf("Мужской", "Женский"), selectedIndex = when (state.gender) {
                    Gender.MALE -> 0
                    Gender.FEMALE -> 1
                    null -> -1 // ✅ Ничего не выбрано
                }, onSelected = { index ->
                    val gender = if (index == 0) Gender.MALE else Gender.FEMALE
                    onIntent(BookingSettingsIntent.GenderSelected(gender))
                })

            Spacer(modifier = Modifier.height(24.dp))

            ScrollablePickers(state = state, onIntent = onIntent)

            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = "Далее",
                enabled = state.isNextEnabled, // ✅ false по умолчанию
                onClick = { onIntent(BookingSettingsIntent.NextClicked) },
            )
        }
    }
}

@Composable
fun BookingTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = "Назад"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SegmentedToggle(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFFE5E5EA)), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(999.dp))
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable { onSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center) {
                Text(
                    text = label,
                    color = if (selected) Color.White
                    else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ScrollablePickers(
    state: BookingSettingsState,
    onIntent: (BookingSettingsIntent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NumberField(
            label = "Возраст (лет)",
            value = state.age,
            unit = "",
            onValueChange = { onIntent(BookingSettingsIntent.AgeChanged(it)) })
        NumberField(
            label = "Рост (см)", value = state.heightCm, unit = "см", onValueChange = {
                onIntent(
                    BookingSettingsIntent.HeightChanged(
                        it
                    )
                )
            })
        NumberField(
            label = "Вес (кг)",
            value = state.weightKg,
            unit = "кг",
            onValueChange = { onIntent(BookingSettingsIntent.WeightChanged(it)) })
        NumberField(
            label = "Размер обуви", value = state.shoeSize, unit = "EU", onValueChange = {
                onIntent(
                    BookingSettingsIntent.ShoeSizeChanged(
                        it
                    )
                )
            })
        NumberField(
            label = "Размер шапки",
            value = state.hatSizeCm,
            unit = "см",
            onValueChange = { onIntent(BookingSettingsIntent.HatSizeChanged(it)) })
    }
}

@Composable
private fun NumberField(
    label: String,
    value: Int,
    unit: String,
    onValueChange: (Int) -> Unit,
) {
    Column {
        Text(
            text = label, style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = if (value > 0) value.toString() else "", // ✅ Пустая строка если 0
            onValueChange = { text ->
                val digits = text.filter { it.isDigit() }
                if (digits.isNotEmpty()) {
                    val number = digits.toIntOrNull()
                    number?.let { onValueChange(it) }
                } else {
                    onValueChange(0)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                if (unit.isNotEmpty()) {
                    Text(
                        text = unit, color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp
                    )
                }
            })
    }
}


@Composable
fun PrimaryButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Text(
            text = text,
            color = if (enabled) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
