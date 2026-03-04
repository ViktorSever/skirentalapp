package ru.shkuratov.skirentalapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Палитра в духе гор/снега/отдыха
private val MountainBlue = Color(0xFF3B82F6)      // спокойный горный синий
private val DeepMountainBlue = Color(0xFF1D4ED8)  // более насыщённый акцент
private val SnowWhite = Color(0xFFFFFFFF)        // чисто белый фон
private val IceGray = Color(0xFFE5E7EB)          // ледяной серый для бордеров
private val TextPrimary = Color(0xFF0F172A)      // тёмный текст на светлом фоне
private val TextSecondary = Color(0xFF6B7280)    // вторичный текст
private val RelaxMint = Color(0xFF10B981)        // мятный акцент (отдых/комфорт)

private val LightColors = lightColorScheme(
    primary = MountainBlue,
    onPrimary = Color.White,
    secondary = RelaxMint,
    onSecondary = Color.White,
    background = SnowWhite,
    onBackground = TextPrimary,
    surface = Color.White,
    onSurface = TextPrimary,
    surfaceVariant = IceGray,
    outline = IceGray,
    outlineVariant = IceGray,
)

private val DarkColors = darkColorScheme(
    primary = DeepMountainBlue,
    onPrimary = Color.White,
    secondary = RelaxMint,
    onSecondary = Color.Black,
    background = Color(0xFF020617),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF020617),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF111827),
    outline = Color(0xFF374151),
    outlineVariant = Color(0xFF4B5563),
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

