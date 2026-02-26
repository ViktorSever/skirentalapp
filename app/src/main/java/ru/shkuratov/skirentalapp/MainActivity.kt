package ru.shkuratov.skirentalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

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

    SkiRentalTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AppScaffold(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@Composable
private fun AppScaffold(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    androidx.compose.material3.Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        AppNavHost(
            navController = navController,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.padding(padding)
        )
    }
}

object Routes {
    const val EquipmentSelection = "equipment"
    const val BookingSettings = "booking_settings"
    const val PersonalDetails = "personal_details"
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.EquipmentSelection,
        modifier = modifier
    ) {
        composable(
            route = Routes.EquipmentSelection,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            val vm: EquipmentSelectionViewModel = viewModel()
            val state = vm.state.collectAsState()

            LaunchedEffect(Unit) {
                vm.effects.collectLatest { effect ->
                    when (effect) {
                        is com.example.skirental.effect.EquipmentSelectionEffect.NavigateToBookingSettings ->
                            navController.navigate(Routes.BookingSettings)

                        is com.example.skirental.effect.EquipmentSelectionEffect.ShowError ->
                            snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }

            EquipmentSelectionScreen(
                state = state.value,
                onIntent = vm::handleIntent
            )
        }

        composable(
            route = Routes.BookingSettings,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            val vm: BookingSettingsViewModel = viewModel()
            val state = vm.state.collectAsState()

            LaunchedEffect(Unit) {
                vm.effects.collectLatest { effect ->
                    when (effect) {
                        is com.example.skirental.effect.BookingSettingsEffect.NavigateToPersonalDetails ->
                            navController.navigate(Routes.PersonalDetails)

                        is com.example.skirental.effect.BookingSettingsEffect.ShowError ->
                            snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }

            BookingSettingsScreen(
                state = state.value,
                onIntent = vm::handleIntent,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.PersonalDetails,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            val vm: PersonalDetailsViewModel = viewModel()
            val state = vm.state.collectAsState()

            LaunchedEffect(Unit) {
                vm.effects.collectLatest { effect ->
                    when (effect) {
                        is com.example.skirental.effect.PersonalDetailsEffect.ShowBookingSuccess -> {
                            snackbarHostState.showSnackbar("Booking confirmed")
                            navController.popBackStack(Routes.EquipmentSelection, inclusive = false)
                        }

                        is com.example.skirental.effect.PersonalDetailsEffect.ShowError ->
                            snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }

            PersonalDetailsScreen(
                state = state.value,
                onIntent = vm::handleIntent,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

