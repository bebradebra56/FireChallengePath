package com.firechalang.pathapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class FireColors(
    val fireRed: Color = FireRed,
    val fireOrange: Color = FireOrange,
    val fireYellow: Color = FireYellow,
    val deepRed: Color = DeepRed,
    val darkRed: Color = DarkRed,
    val accentGold: Color = AccentGold,
    val accentEmber: Color = AccentEmber,
    val accentFlame: Color = AccentFlame,
    val successGreen: Color = SuccessGreen,
    val warningOrange: Color = WarningOrange,
    val errorRed: Color = ErrorRed
)

val LocalFireColors = staticCompositionLocalOf { FireColors() }

private val FireColorScheme = darkColorScheme(
    primary = FireRed,
    onPrimary = TextPrimary,
    primaryContainer = DarkRed,
    onPrimaryContainer = TextPrimary,
    
    secondary = FireOrange,
    onSecondary = TextPrimary,
    secondaryContainer = BackgroundCardElevated,
    onSecondaryContainer = TextPrimary,
    
    tertiary = FireYellow,
    onTertiary = BackgroundDark,
    tertiaryContainer = BackgroundCardElevated,
    onTertiaryContainer = TextPrimary,
    
    background = BackgroundDark,
    onBackground = TextPrimary,
    
    surface = BackgroundCard,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundCardElevated,
    onSurfaceVariant = TextSecondary,
    
    error = ErrorRed,
    onError = TextPrimary,
    
    outline = TextTertiary,
    outlineVariant = BackgroundCardElevated
)

@Composable
fun FireChallengePathTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalFireColors provides FireColors()
    ) {
        MaterialTheme(
            colorScheme = FireColorScheme,
            typography = Typography,
            content = content
        )
    }
}

object FireTheme {
    val colors: FireColors
        @Composable
        get() = LocalFireColors.current
}
