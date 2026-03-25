package com.kus.feature.tier.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kus.core.serialization.KusJson
import com.kus.feature.tier.TierKeys
import com.kus.feature.tier.config.TierKeys.TIER_INITIAL_JSON
import com.kus.feature.tier.ui.TierFilterState
import com.kus.feature.tier.ui.category.TierCategorySelectScreen
import kotlinx.serialization.Serializable

@Serializable
data object Tier

@Serializable
data object TierCategorySelect

fun NavGraphBuilder.tierMainNavGraph(
    onShowMessage: (String) -> Unit,
    navigateToTierCategorySelect: (TierFilterState) -> Unit,
    navigateToDetail: (Long, Boolean) -> Unit,
) {
    composable<Tier> { entry ->
        val initialJson by entry.savedStateHandle
            .getStateFlow<String?>(TIER_INITIAL_JSON, null)
            .collectAsStateWithLifecycle()

        val resultJson by entry.savedStateHandle
            .getStateFlow<String?>(TierKeys.RESULT_FILTER, null)
            .collectAsStateWithLifecycle()

        val initialFilter = initialJson?.let { json ->
            runCatching { KusJson.json.decodeFromString<TierFilterState>(json) }.getOrNull()
        }

        val resultFilter: TierFilterState? = resultJson?.let { json ->
            runCatching { KusJson.json.decodeFromString<TierFilterState>(json) }.getOrNull()
        }

        TierRoute(
            initialFilter = initialFilter,
            navigateToTierCategorySelect = navigateToTierCategorySelect,
            navigateToDetail = navigateToDetail,
            resultFilter = resultFilter,
            consumeInitial = { entry.savedStateHandle[TIER_INITIAL_JSON] = null },
            consumeResult = { entry.savedStateHandle[TierKeys.RESULT_FILTER] = null },
            onShowMessage = onShowMessage
        )
    }
}

fun NavGraphBuilder.tierFullscreenNavGraph(
    initialProvider: () -> TierFilterState,
    popBackStackWithResult: (TierFilterState) -> Unit,
    onBackButtonClick: () -> Unit = {},
) {
    composable<TierCategorySelect> {
        TierCategorySelectScreen(
            initial = initialProvider(),
            onBack = onBackButtonClick,
            onApply = popBackStackWithResult
        )
    }
}
