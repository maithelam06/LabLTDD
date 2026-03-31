package com.example.lab7.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Emerald,
    secondary = Mint,
    tertiary = Sky
)

private val LightColorScheme = lightColorScheme(
    primary = Emerald,
    onPrimary = White,
    primaryContainer = Mint,
    onPrimaryContainer = Ink,
    secondary = EmeraldDark,
    onSecondary = White,
    tertiary = Sky,
    background = White,
    onBackground = Ink,
    surface = White,
    onSurface = Ink,
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF3F7F5),
    onSurfaceVariant = Slate,
    error = Coral,
    onError = White
)

@Composable
fun Lab7Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
