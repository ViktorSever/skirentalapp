package ru.shkuratov.skirentalapp.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skirental.intent.EquipmentSelectionIntent
import com.example.skirental.model.EquipmentType
import com.example.skirental.state.EquipmentSelectionState
import ru.shkuratov.skirentalapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentSelectionScreen(
    state: EquipmentSelectionState,
    onIntent: (EquipmentSelectionIntent) -> Unit,
    onProfileClick: () -> Unit,
) {
    val contentState = state as? EquipmentSelectionState.Content
    val isEquipmentSelected = contentState?.selectedEquipment != null

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = { Text("Выбор оборудования") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Профиль"
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = 0.33f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                trackColor = MaterialTheme.colorScheme.surface,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Выберите снаряжение для начала приключения",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundEquipmentCard(
                    equipmentType = EquipmentType.SKIS,
                    isSelected = contentState?.selectedEquipment == EquipmentType.SKIS,
                    onClick = {
                        onIntent(EquipmentSelectionIntent.SelectEquipment(EquipmentType.SKIS))
                    })

                Spacer(modifier = Modifier.size(32.dp))

                RoundEquipmentCard(
                    equipmentType = EquipmentType.SNOWBOARD,
                    isSelected = contentState?.selectedEquipment == EquipmentType.SNOWBOARD,
                    onClick = {
                        onIntent(EquipmentSelectionIntent.SelectEquipment(EquipmentType.SNOWBOARD))
                    })
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "Далее",
                enabled = isEquipmentSelected,
                onClick = { onIntent(EquipmentSelectionIntent.NextClicked) },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Шаг 1 из 3: Тип оборудования",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Composable
fun RoundEquipmentCard(
    equipmentType: EquipmentType,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val title = if (equipmentType == EquipmentType.SKIS) "Лыжи" else "Сноуборд"
    val imageResId = if (equipmentType == EquipmentType.SKIS) {
        R.drawable.ski
    } else {
        R.drawable.board
    }

    // ✅ Увеличены размеры на 25%
    val cardSize = if (isSelected) 175.dp else 150.dp      // +25%: 140→175, 120→150
    val imageSize = if (isSelected) 100.dp else 87.dp      // +25%: 80→100, 70→87

    Box(
        modifier = Modifier
            .size(cardSize)
            .scale(if (isSelected) 1.1f else 1f)
            .clip(CircleShape)
            .graphicsLayer {
                alpha = if (isSelected) 1f else 0.85f
            }
            .clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        // Белый круглый фон
        Box(
            modifier = Modifier
                .size(cardSize)
                .clip(CircleShape)
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else Color.White, shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // ✅ Увеличен padding для больших карточек
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Изображение из ресурсов
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                modifier = Modifier
                    .size(imageSize)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(if (isSelected) 16.dp else 12.dp))

            // Текст под изображением
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy( // ✅ Больший текст
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = if (isSelected) 16.sp else 14.sp // ✅ Увеличен текст
            )
        }
    }
}
