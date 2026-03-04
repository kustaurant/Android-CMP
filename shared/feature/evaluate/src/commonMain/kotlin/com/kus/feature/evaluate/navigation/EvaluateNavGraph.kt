package com.kus.feature.evaluate.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Evaluate

fun NavGraphBuilder.evaluateNavGraph(
    onBackClick: () -> Unit,
) {
    composable<Evaluate> {
        EvaluateRoute(
            onBackClick = onBackClick,
        )
    }
}
