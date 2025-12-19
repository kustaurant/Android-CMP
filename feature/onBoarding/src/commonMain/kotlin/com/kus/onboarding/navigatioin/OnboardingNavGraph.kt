package com.kus.onboarding.navigatioin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Onboarding


fun NavGraphBuilder.onboardingNavGraph(
    navigateToLogin: () -> Unit,
) {
    composable<Onboarding> {
        OnboardingRoute(
            navigateToLogin = navigateToLogin,
        )
    }
}