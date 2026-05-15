package com.example.flashmorse.presentation.navigation

import com.example.flashmorse.R

/**
 * Top navigation tabs + app navigation container.
 */
sealed class NavigationTab(
    val screen: Screen,
    val label: String,
    val icon: Int,
    val rotation: Float = 0f,
) {

    data object Send : NavigationTab(
        screen = Screen.Sender,
        label = "SEND",
        icon = R.drawable.signal,
        rotation = 270f,
    )

    data object Receive : NavigationTab(
        screen = Screen.Receiver,
        label = "RECEIVE",
        icon = R.drawable.signal,
        rotation = 90f,
    )

    companion object {
        val tabs = listOf(
            Send,
            Receive,
        )
    }
}
