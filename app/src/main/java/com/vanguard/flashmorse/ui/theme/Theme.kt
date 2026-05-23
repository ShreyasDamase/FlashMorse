package com.vanguard.flashmorse.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = FlashOrange,
    secondary = DarkSecondaryText,
    tertiary = ReceiverGreen,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = FlashOrange,
    secondary = LightSecondaryText,
    tertiary = ReceiverGreen,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    outline = LightOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
