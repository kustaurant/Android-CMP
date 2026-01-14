package com.kus.feature.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Login

fun NavGraphBuilder.loginNavGraph(
    navigateToHome: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    composable<Login> {
        LoginRoute(
            navigateToHome = navigateToHome,
        )
    }
}