package com.kus.feature.detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kus.feature.detail.ui.DetailRoute
import com.kus.shared.domain.model.detail.RestaurantDetail
import kotlinx.serialization.Serializable

@Serializable
data class Detail(val restaurantId: Long)

fun NavGraphBuilder.detailNavGraph(
    navigateToUp: () -> Unit,
    navigateToEvaluate: (RestaurantDetail) -> Unit,
) {
    composable<Detail> { backStackEntry ->
        val route = backStackEntry.toRoute<Detail>()
        DetailRoute(
            restaurantId = route.restaurantId,
            navigateToEvaluate = navigateToEvaluate,
            navigateToUp = navigateToUp,
        )
    }
}
