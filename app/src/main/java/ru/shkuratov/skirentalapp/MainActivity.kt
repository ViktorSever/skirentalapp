package ru.shkuratov.skirentalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skirental.UserPreferences
import com.example.skirental.data.AuthRepositoryImpl
import com.example.skirental.data.BookingRepositoryImpl
import com.example.skirental.data.RentalRepositoryImpl
import com.example.skirental.effect.BookingSettingsEffect
import com.example.skirental.effect.EquipmentSelectionEffect
import com.example.skirental.effect.PersonalDetailsEffect
import com.example.skirental.effect.ProfileEffect
import com.example.skirental.store.AuthStore
import com.example.skirental.store.BookingSettingsStore
import com.example.skirental.store.BookingStore
import com.example.skirental.store.PersonalDetailsStore
import com.example.skirental.store.ProfileStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.shkuratov.skirentalapp.screens.AuthScreen
import ru.shkuratov.skirentalapp.screens.BookingAboutScreen
import ru.shkuratov.skirentalapp.screens.BookingSettingsScreen
import ru.shkuratov.skirentalapp.screens.BookingsScreen
import ru.shkuratov.skirentalapp.screens.EquipmentSelectionScreen
import ru.shkuratov.skirentalapp.screens.ProfileScreen
import ru.shkuratov.skirentalapp.screens.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkiRentalApp()
        }
    }
}

@Composable
fun SkiRentalApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val userPreferences = remember { SharedPreferencesUserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = userPreferences.getUserToken()
            if (token != null) {
                navController.navigate("equipment") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        }
    }

    val authStore = remember {
        AuthStore(AuthRepositoryImpl(), userPreferences)
    }

    SkiRentalTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AppScaffold(
                navController = navController,
                snackbarHostState = snackbarHostState,
                authStore = authStore,
                userPreferences = userPreferences
            )
        }
    }
}

@Composable
private fun AppScaffold(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    authStore: AuthStore,
    userPreferences: UserPreferences,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        AppNavHost(
            navController = navController,
            snackbarHostState = snackbarHostState,
            authStore = authStore,
            userPreferences = userPreferences,
            modifier = Modifier.padding(padding)
        )
    }
}

object Routes {
    const val Auth = "auth"
    const val Register = "register"
    const val EquipmentSelection = "equipment"
    const val BookingSettings = "booking_settings"
    const val PersonalDetails = "personal_details"
    const val Profile = "profile"
    const val Bookings = "bookings"
    const val BookingDetails = "booking_details/{bookingId}"
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    authStore: AuthStore,
    userPreferences: UserPreferences,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = Routes.Auth, modifier = modifier
    ) {
        // ✅ Auth & Register
        composable(route = Routes.Auth) {
            AuthScreen(
                onLoginSuccess = { phone ->
                    navController.navigate(Routes.EquipmentSelection) {
                        popUpTo(Routes.Auth) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.Register) },
                store = authStore
            )
        }

        composable(route = Routes.Register) {
            RegisterScreen(
                onRegistrationSuccess = { phone ->
                    navController.navigate(Routes.EquipmentSelection) {
                        popUpTo(Routes.Register) { inclusive = true }
                    }
                }, store = authStore
            )
        }

        // ✅ Equipment Selection
        composable(route = Routes.EquipmentSelection) {
            val vm: EquipmentSelectionViewModel = viewModel()
            val state by vm.state.collectAsState()

            LaunchedEffect(Unit) {
                vm.effects.collectLatest { effect ->
                    when (effect) {
                        is EquipmentSelectionEffect.NavigateToBookingSettings -> navController.navigate(
                            Routes.BookingSettings
                        )

                        is EquipmentSelectionEffect.ShowError -> snackbarHostState.showSnackbar(
                            effect.message
                        )
                    }
                }
            }

            EquipmentSelectionScreen(
                state = state,
                onIntent = vm::handleIntent,
                onProfileClick = { navController.navigate(Routes.Profile) })
        }

        // ✅ Booking Settings
        composable(route = Routes.BookingSettings) {
            val scope = rememberCoroutineScope()
            val bookingSettingsStore = remember { BookingSettingsStore(scope) }
            val state by bookingSettingsStore.state.collectAsState()

            LaunchedEffect(Unit) {
                bookingSettingsStore.effects.collectLatest { effect ->
                    when (effect) {
                        is BookingSettingsEffect.NavigateToPersonalDetails -> navController.navigate(
                            Routes.PersonalDetails
                        )

                        is BookingSettingsEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }

            BookingSettingsScreen(
                state = state,
                onIntent = { scope.launch { bookingSettingsStore.reduce(it) } },
                onBack = { navController.popBackStack() })
        }

        // ✅ Personal Details → Bookings (from_profile = false)
        composable(route = Routes.PersonalDetails) {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            val personalDetailsStore = remember {
                PersonalDetailsStore(
                    scope = scope,
                    repository = RentalRepositoryImpl(),
                    userPreferences = SharedPreferencesUserPreferences(context),
                )
            }
            val state by personalDetailsStore.state.collectAsState()

            LaunchedEffect(Unit) {
                personalDetailsStore.effects.collectLatest { effect ->
                    when (effect) {
                        is PersonalDetailsEffect.ShowBookingSuccess -> {
                            snackbarHostState.showSnackbar("Бронирование подтверждено")
                            navController.popBackStack(Routes.EquipmentSelection, inclusive = false)
                        }

                        is PersonalDetailsEffect.NavigateToBookings -> {
                            // ✅ ИЗ PersonalDetails → Bookings (from_profile = false)
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "from_profile", false
                            )
                            navController.navigate(Routes.Bookings) {
                                popUpTo(Routes.PersonalDetails) { inclusive = true }
                            }
                        }

                        is PersonalDetailsEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }

            PersonalDetailsScreen(
                state = state,
                onIntent = { scope.launch { personalDetailsStore.handleIntent(it) } },
                onBack = { navController.popBackStack() })
        }

        // ✅ Profile → Bookings (from_profile = true)
        composable(route = Routes.Profile) {
            val scope = rememberCoroutineScope()
            val profileStore = remember { ProfileStore(scope = scope, userPreferences) }
            val profileState by profileStore.state.collectAsState()

            LaunchedEffect(profileStore) {
                profileStore.effects.collectLatest { effect ->
                    when (effect) {
                        is ProfileEffect.NavigateToBookings -> {
                            // ✅ ИЗ Profile → Bookings (from_profile = true)
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "from_profile", true
                            )
                            navController.navigate(Routes.Bookings)
                        }

                        is ProfileEffect.LogoutSuccess -> {
                            navController.navigate(Routes.Auth) {
                                popUpTo(Routes.EquipmentSelection) { inclusive = true }
                            }
                        }
                    }
                }
            }

            ProfileScreen(
                state = profileState,
                onIntent = { scope.launch { profileStore.reduce(it) } },
                onBackClick = { navController.popBackStack() })
        }

        composable(route = Routes.Bookings) {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            val bookingStore = remember {
                BookingStore(
                    scope = scope,
                    userPreferences = SharedPreferencesUserPreferences(context),
                    bookingRepository = BookingRepositoryImpl()
                )
            }
            val bookingState by bookingStore.state.collectAsState()

            val fromProfile =
                navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("from_profile")
                    ?: navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("from_profile")
                    ?: false

            BookingsScreen(
                state = bookingState,
                onIntent = { scope.launch { bookingStore.reduce(it) } },
                onBackClick = {
                    if (fromProfile) {
                        // ✅ Из Profile → назад в Profile
                        navController.popBackStack(Routes.Profile, inclusive = false)
                    } else {
                        // ✅ Из PersonalDetails → на EquipmentSelection
                        navController.navigate(Routes.EquipmentSelection) {
                            popUpTo(Routes.Bookings) { inclusive = true }
                        }
                    }
                },
                onBookingClick = { booking ->
                    navController.navigate("booking_details/${booking.id}")
                })
        }

        // ✅ Booking Details
        composable(
            route = Routes.BookingDetails,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingAboutScreen(
                bookingId = bookingId, navController = navController
            )
        }
    }
}
