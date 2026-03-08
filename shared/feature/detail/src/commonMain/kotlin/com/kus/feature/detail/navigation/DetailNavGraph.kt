package com.kus.feature.detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kus.feature.detail.ui.DetailRoute
import kotlinx.serialization.Serializable

@Serializable
data object Detail

fun NavGraphBuilder.detailNavGraph(
    navigateToUp: () -> Unit,
    navigateToEvaluate: () -> Unit,
) {
    composable<Detail> {
        DetailRoute(
            navigateToEvaluate = navigateToEvaluate,
            navigateToUp = navigateToUp,
        )
    }
}
