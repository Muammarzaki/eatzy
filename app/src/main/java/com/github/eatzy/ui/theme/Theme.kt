package com.github.eatzy.ui.theme

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

private val DarkColorScheme = darkColorScheme()


private val LightColorScheme = lightColorScheme(
    // Primary colors - Using your main green accent
    primary = AccentColor1,
    onPrimary = ContentColor,
    primaryContainer = ActiveTabColor,
    onPrimaryContainer = DarkGreen,

    // Secondary colors - Using your blue accent
    secondary = AccentColor2,
    onSecondary = ContentColor,
    secondaryContainer = LightBlue,
    onSecondaryContainer = DarkBlue,

    // Tertiary colors - Using your container colors
    tertiary = ContainerDark,
    onTertiary = ContentColor,
    tertiaryContainer = ContainerColor1,
    onTertiaryContainer = ContainerColor2,

    // Error colors - Using your danger colors
    error = Danger,
    onError = ContentColor,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Background colors
    background = BackgroundColor,
    onBackground = MainContentColorOnWhite,

    // Surface colors
    surface = BackgroundColor,
    onSurface = MainContentColorOnWhite,
    surfaceVariant = LightGreen,
    onSurfaceVariant = Color(0xFF424940),

    // Surface tint
    surfaceTint = AccentColor1,

    // Inverse colors
    inverseSurface = Color(0xFF2F312E),
    inverseOnSurface = Color(0xFFF0F1EC),
    inversePrimary = ActiveTabColor,

    // Outline colors
    outline = SubtitleTextColor,
    outlineVariant = Color(0xFFC2C8BC),

    // Scrim
    scrim = Color(0xFF000000)
)


@Composable
fun EaTzyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        true -> LightColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}