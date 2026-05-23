package com.vanguard.flashmorse.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vanguard.flashmorse.presentation.communicator.CommunicatorScreen
import com.vanguard.flashmorse.presentation.settings.SettingsScreen
import com.vanguard.flashmorse.presentation.policy.PrivacyPolicyScreen
import com.vanguard.flashmorse.presentation.guide.MorseGuideScreen
import com.vanguard.flashmorse.presentation.history.HistoryScreen
import com.vanguard.flashmorse.presentation.about.AboutScreen

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
            CommunicatorScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings)
                },
                onNavigateToMorseGuide = {
                    navController.navigate(Screen.MorseGuide)
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigate(Screen.PrivacyPolicy)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History)
                },
                onNavigateToAbout = {
                    navController.navigate(Screen.About)
                }
            )
        }
        
        composable<Screen.Settings> {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.PrivacyPolicy> {
            PrivacyPolicyScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.MorseGuide> {
            MorseGuideScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.History> {
            HistoryScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.About> {
            AboutScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
