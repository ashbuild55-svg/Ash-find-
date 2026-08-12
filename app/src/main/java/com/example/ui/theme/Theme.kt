package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AshFindesColorScheme = darkColorScheme(
    primary = CyanPrimary,
    onPrimary = ObsidianBg,
    primaryContainer = CardSurfaceElevated,
    onPrimaryContainer = CyanGlow,
    secondary = ElectricBlue,
    onSecondary = TextPrimary,
    secondaryContainer = CardSurface,
    onSecondaryContainer = TextSecondary,
    tertiary = DeepIndigo,
    onTertiary = TextPrimary,
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = CardSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder,
    outlineVariant = GlassBorderActive,
    error = ErrorRed
)

@Composable
fun AshFindesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = AshFindesColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ObsidianBg.toArgb()
            window.navigationBarColor = DarkNavyBg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
