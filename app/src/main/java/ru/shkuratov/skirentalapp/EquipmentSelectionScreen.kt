package ru.shkuratov.skirentalapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skirental.intent.EquipmentSelectionIntent
import com.example.skirental.model.EquipmentType
import com.example.skirental.state.EquipmentSelectionState

@Composable
fun EquipmentSelectionScreen(
    state: EquipmentSelectionState,
    onIntent: (EquipmentSelectionIntent) -> Unit
) {
    val contentState = state as? EquipmentSelectionState.Content

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Equipment",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        androidx.compose.material3.LinearProgressIndicator(
            progress = 0.33f,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            trackColor = MaterialTheme.colorScheme.surface,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Choose your gear to start the adventure",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        EquipmentCard(
            title = "Skis",
            subtitle = "High-quality rental gear",
            isSelected = contentState?.selectedEquipment == EquipmentType.SKIS,
            onClick = {
                onIntent(EquipmentSelectionIntent.SelectEquipment(EquipmentType.SKIS))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        EquipmentCard(
            title = "Snowboard",
            subtitle = "High-quality rental gear",
            isSelected = contentState?.selectedEquipment == EquipmentType.SNOWBOARD,
            onClick = {
                onIntent(EquipmentSelectionIntent.SelectEquipment(EquipmentType.SNOWBOARD))
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = "Next",
            enabled = contentState?.isNextEnabled == true,
            onClick = { onIntent(EquipmentSelectionIntent.NextClicked) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Step 1 of 3: Equipment Type",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
private fun EquipmentCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

