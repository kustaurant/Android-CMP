package com.kus.feature.community.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Community

fun NavGraphBuilder.communityNavGraph(
    onShowMessage: (String) -> Unit,
) {
    composable<Community> {
        CommunityRoute( 
        )
    }
}