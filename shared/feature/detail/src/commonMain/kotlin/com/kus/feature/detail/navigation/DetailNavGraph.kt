package com.kus.feature.detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kus.feature.detail.ui.DetailRoute
import com.kus.shared.domain.model.detail.RestaurantDetail
import kotlinx.serialization.Serializable

@Serializable
data object Detail

fun NavGraphBuilder.detailNavGraph(
    navigateToUp: () -> Unit,
    navigateToEvaluate: (RestaurantDetail) -> Unit,
) {
    composable<Detail> {
        DetailRoute(
            navigateToEvaluate = navigateToEvaluate,
            navigateToUp = navigateToUp,
        )
    }
}
