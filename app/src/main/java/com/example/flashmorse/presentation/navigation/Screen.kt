package com.example.flashmorse.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Main : Screen

    @Serializable
    data object Sender : Screen
    
    @Serializable
    data object Receiver : Screen
}
