package com.kus.feature.tier.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kus.feature.tier.ui.TierFilterState
import com.kus.feature.tier.ui.category.TierCategorySelectScreen
import kotlinx.serialization.Serializable

@Serializable
data object Tier

@Serializable
data object TierCategorySelect

fun NavGraphBuilder.tierNavGraph(
    initialProvider: () -> TierFilterState,
    navigateToTierCategorySelect: (TierFilterState) -> Unit,
    popBackStackWithResult: (TierFilterState) -> Unit,
) {
    composable<Tier> { entry ->
        val result by entry.savedStateHandle
            .getStateFlow<TierFilterState?>("result", null)
            .collectAsStateWithLifecycle()

        TierRoute(
            navigateToTierCategorySelect = navigateToTierCategorySelect,
            resultFilter = result,
            consumeResult = { entry.savedStateHandle["result"] = null },
        )
    }

    composable<TierCategorySelect> {
        TierCategorySelectScreen(
            initial = initialProvider(),
            onBack = { /* pop */ },
            onApply = { result -> popBackStackWithResult(result) }
        )
    }
}
