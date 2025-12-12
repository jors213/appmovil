package com.example.servicedigital.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val CustomDarkColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    secondary = NeonCyan,
    onSecondary = Color.Black,
    tertiary = DeepBlue,
    background = DarkBackground,
    onBackground = OnDark,
    surface = DarkSurface,
    onSurface = OnDark
)

private val CustomLightColorScheme = lightColorScheme(
    primary = DeepBlue,
    onPrimary = Color.White,
    secondary = NeonPurple,
    onSecondary = Color.White,
    tertiary = NeonCyan,
    background = Color(0xFFF0F2F5),
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun ServiceDigitalTheme(
    // Use ThemeManager.isDarkTheme by default, falling back to system setting logic if needed initially
    // but here we rely on the manager being toggled.
    // We default to isSystemInDarkTheme() only if ThemeManager wasn't explicitly set, 
    // but for simplicity let's make ThemeManager the source of truth for the app switch.
    darkTheme: Boolean = ThemeManager.isDarkTheme, 
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CustomDarkColorScheme
        else -> CustomLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}