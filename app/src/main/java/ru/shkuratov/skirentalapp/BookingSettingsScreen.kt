package ru.shkuratov.skirentalapp

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
        BookingTopBar(
            title = "Booking Settings",
            onBack = onBack
        )

        androidx.compose.material3.LinearProgressIndicator(
            progress = 0.66f,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            trackColor = MaterialTheme.colorScheme.surface,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gender",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        SegmentedToggle(
            options = listOf("Male", "Female"),
            selectedIndex = when (state.gender) {
                Gender.MALE -> 0
                Gender.FEMALE -> 1
                else -> -1
            },
            onSelected = { index ->
                val gender = if (index == 0) Gender.MALE else Gender.FEMALE
                onIntent(BookingSettingsIntent.GenderSelected(gender))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ScrollablePickers(state = state, onIntent = onIntent)

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Next",
            enabled = state.isNextEnabled,
            onClick = { onIntent(BookingSettingsIntent.NextClicked) }
        )
    }
}

@Composable
fun BookingTopBar(
    title: String,
    onBack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Back",
            modifier = Modifier
                .clickable(onClick = onBack),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp
        )
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
            .background(Color(0xFFE5E5EA)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(999.dp))
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .clickable { onSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
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
            label = "Age (Years)",
            value = state.age,
            unit = "",
            onValueChange = { onIntent(BookingSettingsIntent.AgeChanged(it.coerceIn(5, 99))) }
        )
        NumberField(
            label = "Height (cm)",
            value = state.heightCm,
            unit = "CM",
            onValueChange = { onIntent(BookingSettingsIntent.HeightChanged(it.coerceIn(100, 220))) }
        )
        NumberField(
            label = "Weight (kg)",
            value = state.weightKg,
            unit = "KG",
            onValueChange = { onIntent(BookingSettingsIntent.WeightChanged(it.coerceIn(20, 150))) }
        )
        NumberField(
            label = "Shoe Size",
            value = state.shoeSize,
            unit = "EU",
            onValueChange = { onIntent(BookingSettingsIntent.ShoeSizeChanged(it.coerceIn(35, 48))) }
        )
        NumberField(
            label = "Hat Size",
            value = state.hatSizeCm,
            unit = "CM",
            onValueChange = { onIntent(BookingSettingsIntent.HatSizeChanged(it.coerceIn(54, 62))) }
        )
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
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = value.toString(),
            onValueChange = { text ->
                val digits = text.filter { it.isDigit() }
                if (digits.isNotEmpty()) {
                    digits.toIntOrNull()?.let { onValueChange(it) }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            trailingIcon = {
                if (unit.isNotEmpty()) {
                    Text(
                        text = unit,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 14.sp
                    )
                }
            }
        )
    }
}

