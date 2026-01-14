package com.kus.feature.splash.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Splash

fun NavGraphBuilder.splashNavGraph(
    navigateToOnboarding: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<Splash>(
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(tween(0)) },
        popExitTransition = { fadeOut(tween(200)) },
    ) {
        SplashRoute(
            navigateToOnboarding = navigateToOnboarding,
            navigateToLogin = navigateToLogin,
        )
    }
}