package com.flamedate.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val FlameRed = Color(0xFFE91E63)
val FlameRedDark = Color(0xFFC2185B)
val FlameOrange = Color(0xFFFF6F00)
val LikeGreen = Color(0xFF4CAF50)
val NopeRed = Color(0xFFF44336)
val SuperLikeBlue = Color(0xFF2196F3)
val LightBackground = Color(0xFFF5F5F5)
val DarkBackground = Color(0xFF121212)
val LightSurface = Color(0xFFFFFFFF)
val DarkSurface = Color(0xFF1E1E1E)

private val LightColorScheme = lightColorScheme(
    primary = FlameRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0EC),
    secondary = Color(0xFF03DAC5),
    onSecondary = Color.Black,
    background = LightBackground,
    surface = LightSurface,
    error = Color(0xFFB00020),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF8BA7),
    onPrimary = Color.Black,
    primaryContainer = FlameRedDark,
    secondary = Color(0xFF03DAC5),
    onSecondary = Color.Black,
    background = DarkBackground,
    surface = DarkSurface,
    error = Color(0xFFCF6679),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5)
)

@Composable
fun FlameDateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
