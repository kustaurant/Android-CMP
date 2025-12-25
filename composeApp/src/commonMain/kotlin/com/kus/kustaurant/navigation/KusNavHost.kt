package com.kus.kustaurant.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kus.feature.home.navigation.homeNavGraph
import com.kus.feature.login.navigation.Login
import com.kus.feature.login.navigation.loginNavGraph
import com.kus.feature.onboarding.navigatioin.Onboarding
import com.kus.feature.onboarding.navigatioin.onboardingNavGraph
import com.kus.feature.splash.navigation.Splash
import com.kus.feature.splash.navigation.splashNavGraph

@Composable
fun KusNavHost(
    navController: NavHostController,
    durationMillis: Int,
    bottomBarState: MutableState<Boolean>,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Splash,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis)
            )
        },
        modifier = modifier
    ) {
        splashNavGraph(
            navigateToOnboarding = {
                navController.navigate(Onboarding) {
                    popUpTo(Splash) { inclusive = true }
                }
            },
            navigateToLogin = {
                navController.navigate(Login) {
                    popUpTo(Splash) { inclusive = true }
                }
            }
        )

        onboardingNavGraph(
            navigateToLogin = {
                navController.navigate(Login) {
                    popUpTo(Onboarding) { inclusive = true }
                }
            }
        )

        loginNavGraph(
            navigateToHome = {
                // TODO
            },
            onShowMessage = onShowMessage
        )


        homeNavGraph(
            navigateToTier = {
                // TODO
            },
            navigateToEvaluate = {
                // TODO
            }
        )
    }
}