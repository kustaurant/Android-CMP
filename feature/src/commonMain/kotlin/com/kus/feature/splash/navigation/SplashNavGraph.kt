package com.kus.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Splash

fun NavGraphBuilder.splashNavGraph(
    navigateToOnboarding: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<Splash> {
        SplashRoute(
            navigateToOnboarding = navigateToOnboarding,
            navigateToLogin = navigateToLogin,
        )
    }
}