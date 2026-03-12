package com.kus.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kus.feature.home.ui.HomeRoute
import com.kus.shared.domain.model.tier.filter.Cuisine
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavController.navigateToHome(
    navOptions: NavOptions? = null,
) = navigate(Home, navOptions)

fun NavGraphBuilder.homeNavGraph(
    navigateToSearch: () -> Unit,
    navigateToAlert: () -> Unit,
    navigateToTier: (Cuisine) -> Unit,
    navigateToDetail: (Long) -> Unit,
) {
    composable<Home> {
        HomeRoute(
            onSearchNavigate = navigateToSearch,
            onAlertNavigate = navigateToAlert,
            onTierNavigate = navigateToTier,
            onRestaurantDetailNavigate = navigateToDetail,
        )
    }
}
