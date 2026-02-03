package com.kus.feature.tier.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kus.core.serialization.KusJson
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
    onBackButtonClick : () -> Unit = {}
) {
    composable<Tier> { entry ->
        val resultJson by entry.savedStateHandle
            .getStateFlow<String?>("tier_result_json", null)
            .collectAsStateWithLifecycle()

        val result: TierFilterState? = resultJson?.let { json ->
            runCatching { KusJson.json.decodeFromString<TierFilterState>(json) }.getOrNull()
        }

        TierRoute(
            navigateToTierCategorySelect = navigateToTierCategorySelect,
            resultFilter = result,
            consumeResult = { entry.savedStateHandle["tier_result_json"] = null },
        )
    }

    composable<TierCategorySelect> {
        TierCategorySelectScreen(
            initial = initialProvider(),
            onBack = { onBackButtonClick() },
            onApply = { result -> popBackStackWithResult(result) }
        )
    }
}
