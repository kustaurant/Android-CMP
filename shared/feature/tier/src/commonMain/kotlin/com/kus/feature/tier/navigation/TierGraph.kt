package com.kus.feature.tier.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Tier

fun NavGraphBuilder.tierNavGraph(
    onShowMessage: (String) -> Unit,
) {
    composable<Tier> {
        TierRoute(
        )
    }
}