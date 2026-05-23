package com.vanguard.flashmorse.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Main : Screen

    @Serializable
    data object Communicator : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object PrivacyPolicy : Screen

    @Serializable
    data object MorseGuide : Screen

    @Serializable
    data object History : Screen

    @Serializable
    data object About : Screen
}
