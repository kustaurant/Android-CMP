package com.kus.feature.draw.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Draw

fun NavGraphBuilder.drawNavGraph(
    onShowMessage: (String) -> Unit,
) {
    composable<Draw> {
        DrawRoute( 
        )
    }
}