package ru.shkuratov.skirentalapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.skirental.data.BookingRepositoryImpl
import com.example.skirental.intent.BookingIntent
import com.example.skirental.model.Booking
import com.example.skirental.model.BookingStatus
import com.example.skirental.store.BookingStore
import kotlinx.coroutines.launch
import ru.shkuratov.skirentalapp.SharedPreferencesUserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingAboutScreen(
    bookingId: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    // ✅ Новый Store для деталей брони
    val bookingStore = remember {
        BookingStore(
            scope = scope, userPreferences = SharedPreferencesUserPreferences(context),
            BookingRepositoryImpl(),
        )
    }

    var currentBooking by remember { mutableStateOf<Booking?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // ✅ Загружаем список и ищем нужную бронь
    LaunchedEffect(bookingId) {
        bookingStore.reduce(BookingIntent.LoadBookings)
    }

    // ✅ Наблюдаем за списком броней
    val bookingState by bookingStore.state.collectAsState()
    LaunchedEffect(bookingState.bookings) {
        currentBooking = bookingState.bookings.find { it.id == bookingId }
        if (currentBooking != null) {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = { Text("Детали брони") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }) { paddingValues ->
        if (isLoading || currentBooking == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
//                CircularProgressIndicator()
            }
        } else {
            currentBooking?.let { booking ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        item {
                            InfoField("Тип оборудования", booking.equipmentType)
                            InfoField("Пол", if (booking.gender == "MALE") "Мужской" else "Женский")
                            InfoField("Возраст", "${booking.age} лет")
                            InfoField("Рост", "${booking.heightCm} см")
                            InfoField("Вес", "${booking.weightKg} кг")
                            InfoField("Размер обуви", "${booking.shoeSize}")
                            InfoField("Размер шапки", "${booking.hatSizeCm} см")
                            InfoField("ФИО", booking.fullName)
                            InfoField("Телефон", booking.phone)
                            InfoField("Дата", booking.date)
                            InfoField("Время", booking.timeSlot)
                        }
                    }
                    when (booking.getStatusEnum()) {
                        BookingStatus.NEW -> Button(
                            onClick = {
                                scope.launch {
                                    bookingStore.reduce(BookingIntent.CancelBooking(booking.id))
                                }
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ), modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Отменить бронь", color = Color.White)
                        }

                        BookingStatus.CONFIRMED -> Button(
                            onClick = {
                                scope.launch {
                                    bookingStore.reduce(BookingIntent.ReturnBooking(booking.id))
                                }
                            }, modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Сдать оборудование")
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoField(label: String, value: String) {
    Box {
        OutlinedTextField(
            value = value,  // ✅ Используем state из ViewModel
            onValueChange = { },        // ✅ Пустая функция = НЕ редактируется
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,            // ✅ Не редактируется
            singleLine = true,
            enabled = false,
        )
    }
}
