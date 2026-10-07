package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AccentPurple,
    onPrimary = Color.White,
    primaryContainer = AccentPurpleLight,
    onPrimaryContainer = AccentPurple,
    secondary = AccentGreen,
    onSecondary = Color.White,
    tertiary = AccentRed,
    onTertiary = Color.White,
    background = BackgroundColor,
    onBackground = TextMain,
    surface = CardBackground,
    onSurface = TextMain,
    surfaceVariant = Color.White,
    onSurfaceVariant = TextMuted,
    outline = BorderColor
)

@Composable
fun FitHubTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
