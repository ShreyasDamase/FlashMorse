package com.vanguard.flashmorse.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val FlashOrange = Color(0xFFFFA500)
val LightBackground = Color(0xFFE8DFD3)
val LightSurface = Color(0xFFF2ECE4)
val LightOnSurface = Color(0xFF4A453E)
val LightSecondaryText = Color(0xFF8B8479)
val LightOutline = Color(0xFFD1C7B7)
val LightBodyText = Color.DarkGray
val LightMutedText = Color.Gray
val LightSwitchThumb = Color.White
val LightSwitchTrack = Color(0xFFCCCCCC)

val DarkBackground = Color(0xFF000000)
val DarkSurface = Color(0xFF121212)
val DarkOnSurface = Color(0xFFFFFFFF)
val DarkSecondaryText = Color(0xFFB0B0B0)
val DarkOutline = Color(0xFF222222)
val DarkBodyText = Color(0xFFE0E0E0)
val DarkMutedText = Color(0xFF888888)
val DarkSwitchThumb = Color.DarkGray
val DarkSwitchTrack = Color(0xFF222222)

val InputSurface = Color(0xFF1E1E1E)
val ReceiverGreen = Color(0xFF90EE90)
val HistoryReceivedGreen = Color(0xFF4CAF50)

val ColorScheme.appSecondaryText: Color
    get() = if (background == DarkBackground) DarkSecondaryText else LightSecondaryText

val ColorScheme.appBodyText: Color
    get() = if (background == DarkBackground) DarkBodyText else LightBodyText

val ColorScheme.appMutedText: Color
    get() = if (background == DarkBackground) DarkMutedText else LightMutedText

val ColorScheme.appInputSurface: Color
    get() = InputSurface

val ColorScheme.appReceiverGreen: Color
    get() = ReceiverGreen

val ColorScheme.appHistoryReceivedGreen: Color
    get() = HistoryReceivedGreen

val ColorScheme.appSwitchUncheckedThumb: Color
    get() = if (background == DarkBackground) DarkSwitchThumb else LightSwitchThumb

val ColorScheme.appSwitchUncheckedTrack: Color
    get() = if (background == DarkBackground) DarkSwitchTrack else LightSwitchTrack

val ColorScheme.appDrawerSelectedContainer: Color
    get() = if (background == DarkBackground) DarkOutline else LightBackground

val ColorScheme.appDrawerSelectedText: Color
    get() = if (background == DarkBackground) FlashOrange else LightOnSurface
