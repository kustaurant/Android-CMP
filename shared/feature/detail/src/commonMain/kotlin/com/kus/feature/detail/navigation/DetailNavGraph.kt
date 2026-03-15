package com.kus.feature.detail.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kus.feature.detail.config.DetailKeys
import com.kus.feature.detail.ui.DetailRoute
import com.kus.shared.domain.model.detail.RestaurantDetail
import kotlinx.serialization.Serializable

@Serializable
data class Detail(val restaurantId: Long)

fun NavController.navigateToDetail(
    restaurantId: Long,
    navOptions: NavOptions?= null,
) = navigate(Detail(restaurantId), navOptions)

fun NavGraphBuilder.detailNavGraph(
    navigateToUp: () -> Unit,
    navigateToEvaluate: (RestaurantDetail) -> Unit,
) {
    composable<Detail> { backStackEntry ->
        val route = backStackEntry.toRoute<Detail>()
        val shouldRefreshFromEvaluate by backStackEntry.savedStateHandle
            .getStateFlow(DetailKeys.DETAIL_EVALUATE_REFRESH, false)
            .collectAsStateWithLifecycle()

        DetailRoute(
            restaurantId = route.restaurantId,
            navigateToEvaluate = navigateToEvaluate,
            navigateToUp = navigateToUp,
            shouldRefreshFromEvaluate = shouldRefreshFromEvaluate,
            clearEvaluateRefreshFlag = {
                backStackEntry.savedStateHandle[DetailKeys.DETAIL_EVALUATE_REFRESH] = false
            },
        )
    }
}
