package com.kus.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kus.feature.search.ui.SearchRoute
import kotlinx.serialization.Serializable

@Serializable
data object Search

fun NavController.navigateToSearch(
    navOptions: NavOptions? = null,
) = navigate(Search, navOptions)

fun NavGraphBuilder.searchNavGraph(
    navigateToUp: () -> Unit,
    navigateToRestDetail: (Long) -> Unit,
) {
    composable<Search> {
        SearchRoute(
            onBackClick = navigateToUp,
            onRestDetailNavigate = navigateToRestDetail,
        )
    }
}
