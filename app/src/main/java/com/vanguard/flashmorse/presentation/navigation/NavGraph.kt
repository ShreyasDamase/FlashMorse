package com.vanguard.flashmorse.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    NavHost(
        navController = navController,
        startDestination = Screen.Communicator,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(220)
            ) + fadeIn(animationSpec = tween(180))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(220)
            ) + fadeOut(animationSpec = tween(140))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(220)
            ) + fadeIn(animationSpec = tween(180))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(220)
            ) + fadeOut(animationSpec = tween(140))
        }
    ) {
        composable<Screen.Communicator> {
            CommunicatorScreen(
                onNavigateToSettings = {
                    navController.navigateSafely(Screen.Settings, currentRoute)
                },
                onNavigateToMorseGuide = {
                    navController.navigateSafely(Screen.MorseGuide, currentRoute)
                },
                onNavigateToPrivacyPolicy = {
                    navController.navigateSafely(Screen.PrivacyPolicy, currentRoute)
                },
                onNavigateToHistory = {
                    navController.navigateSafely(Screen.History, currentRoute)
                },
                onNavigateToAbout = {
                    navController.navigateSafely(Screen.About, currentRoute)
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

private fun NavHostController.navigateSafely(
    screen: Screen,
    currentRoute: String?
) {
    val targetRoute = screen::class.qualifiedName ?: return
    if (currentRoute == targetRoute) return

    navigate(screen, navOptions = navOptions {
        launchSingleTop = true
        popUpTo(graph.startDestinationId)
    })
}
