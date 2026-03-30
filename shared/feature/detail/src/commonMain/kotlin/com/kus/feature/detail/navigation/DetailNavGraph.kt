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
data class Detail(
    val restaurantId: Long,
    val isTempTier: Boolean = false,
)

fun NavController.navigateToDetail(
    restaurantId: Long,
    isTempTier: Boolean = false,
    navOptions: NavOptions?= null,
) = navigate(Detail(restaurantId, isTempTier), navOptions)

fun NavGraphBuilder.detailNavGraph(
    navigateToUp: () -> Unit,
    navigateToEvaluate: (RestaurantDetail) -> Unit,
    onShowMessage: (String) -> Unit,
) {
    composable<Detail> { backStackEntry ->
        val route = backStackEntry.toRoute<Detail>()
        val shouldRefreshFromEvaluate by backStackEntry.savedStateHandle
            .getStateFlow(DetailKeys.DETAIL_EVALUATE_REFRESH, false)
            .collectAsStateWithLifecycle()

        DetailRoute(
            restaurantId = route.restaurantId,
            isTempTier = route.isTempTier,
            onShowMessage = onShowMessage,
            navigateToEvaluate = navigateToEvaluate,
            navigateToUp = navigateToUp,
            shouldRefreshFromEvaluate = shouldRefreshFromEvaluate,
            clearEvaluateRefreshFlag = {
                backStackEntry.savedStateHandle[DetailKeys.DETAIL_EVALUATE_REFRESH] = false
            },
        )
    }
}
