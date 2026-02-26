package ru.shkuratov.skirentalapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Primary = Color(0xFF007AFF)
private val BackgroundLight = Color(0xFFFFFFFF)
private val BackgroundDark = Color(0xFF121212)
private val Secondary = Color(0xFF666666)
private val Placeholder = Color(0xFF999999)

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    background = BackgroundLight,
    onBackground = Color.Black,
    surface = BackgroundLight,
    onSurface = Color.Black,
    secondary = Secondary
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = BackgroundDark,
    onSurface = Color.White,
    secondary = Secondary
)

@Composable
fun SkiRentalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}

