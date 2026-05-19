package com.example.flashmorse.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashmorse.presentation.communicator.CommunicatorScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Communicator,
        modifier = modifier,
    ) {
        composable<Screen.Communicator> {
            CommunicatorScreen()
        }
    }
}
