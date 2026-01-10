package com.kus.kustaurant.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kus.feature.community.navigation.communityNavGraph
import com.kus.feature.draw.navigation.drawNavGraph
import com.kus.feature.home.navigation.Home
import com.kus.feature.home.navigation.homeNavGraph
import com.kus.feature.login.navigation.Login
import com.kus.feature.login.navigation.loginNavGraph
import com.kus.feature.my.navigation.myNavGraph
import com.kus.feature.onboarding.navigatioin.Onboarding
import com.kus.feature.onboarding.navigatioin.onboardingNavGraph
import com.kus.feature.splash.navigation.Splash
import com.kus.feature.splash.navigation.splashNavGraph
import com.kus.feature.tier.navigation.tierNavGraph

@Composable
fun KusNavHost(
    navController: NavHostController,
    durationMillis: Int,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Home,

        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis)) +
                    scaleIn(
                        initialScale = 0.98f,
                        animationSpec = tween(durationMillis)
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis)) +
                    scaleOut(
                        targetScale = 0.98f,
                        animationSpec = tween(durationMillis)
                    )
        },

        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis)) +
                    scaleIn(
                        initialScale = 0.98f,
                        animationSpec = tween(durationMillis)
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis)) +
                    scaleOut(
                        targetScale = 0.98f,
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
                navController.navigate(Home) {
                    popUpTo(Login) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onShowMessage = onShowMessage
        )

        homeNavGraph(
            navigateToTier = { /* TODO */ },
            navigateToEvaluate = { /* TODO */ }
        )

        drawNavGraph(onShowMessage = onShowMessage)
        tierNavGraph(onShowMessage = onShowMessage)
        communityNavGraph(onShowMessage = onShowMessage)
        myNavGraph(onShowMessage = onShowMessage)
    }
}