package com.socialquest.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val LightColorScheme = lightColorScheme(
    primary = Coral,
    onPrimary = Color.White,
    primaryContainer = CoralSoft,
    onPrimaryContainer = Ink,
    secondary = Tide,
    onSecondary = Color.White,
    secondaryContainer = TideSoft,
    onSecondaryContainer = Ink,
    tertiary = Lilac,
    onTertiary = Color.White,
    tertiaryContainer = LilacSoft,
    onTertiaryContainer = Ink,
    background = Mist,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Color(0xFFEDE7E1),
    onSurfaceVariant = InkSoft,
    outline = Line,
    error = Color(0xFFB5473D),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF9A85),
    onPrimary = Color(0xFF35130D),
    primaryContainer = Color(0xFF65372F),
    onPrimaryContainer = Color(0xFFFFDAD2),
    secondary = Color(0xFF83C5BB),
    onSecondary = Color(0xFF073B36),
    secondaryContainer = Color(0xFF244F4A),
    onSecondaryContainer = Color(0xFFC2EDE5),
    tertiary = Color(0xFFC3AFE8),
    onTertiary = Color(0xFF302047),
    tertiaryContainer = Color(0xFF49365F),
    onTertiaryContainer = Color(0xFFE9DDFF),
    background = Night,
    onBackground = NightText,
    surface = NightSurface,
    onSurface = NightText,
    surfaceVariant = NightElevated,
    onSurfaceVariant = NightMuted,
    outline = NightLine,
    error = Color(0xFFFFB4AB),
)

private val ColorWhite = androidx.compose.ui.graphics.Color.White

@Composable
fun SocialquestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep the SocialQuest brand consistent across Android devices.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val scheme = when {
        dynamicColor -> if (darkTheme) DarkColorScheme else LightColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        content = content,
    )
}
