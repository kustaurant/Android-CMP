package com.kus.feature.detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Detail

fun NavGraphBuilder.detailNavGraph(
    navigateToEvaluate: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable<Detail> {
        DetailRoute(
            navigateToEvaluate = navigateToEvaluate,
            onBackClick = onBackClick,
        )
    }
}
