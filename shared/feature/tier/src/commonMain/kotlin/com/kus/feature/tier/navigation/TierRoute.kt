package com.kus.feature.tier.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.feature.tier.ui.TierFilterState
import com.kus.feature.tier.ui.TierScreen
import com.kus.feature.tier.ui.TierViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TierRoute(
    navigateToTierCategorySelect: (TierFilterState) -> Unit,
    initialFilter: () -> TierFilterState,
    navigateToDetail: () -> Unit,
    resultFilter: TierFilterState?,
    consumeResult: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel: TierViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val initialFilter = remember { initialFilter() }

    LaunchedEffect(Unit) {
        val isDefault = initialFilter == TierFilterState()
        if (!isDefault) {
            viewModel.setCategory(
                cuisines = initialFilter.cuisines,
                situations = initialFilter.situations,
                locations = initialFilter.locations,
            )
        }
    }

    LaunchedEffect(resultFilter) {
        val result = resultFilter ?: return@LaunchedEffect

        viewModel.setCategory(
            cuisines = result.cuisines,
            situations = result.situations,
            locations = result.locations,
        )

        consumeResult()
    }

    TierScreen(
        onFilterClick = { navigateToTierCategorySelect(uiState.filterState) },
        onShowMessage = onShowMessage,
        onNavigateRestaurantDetail = navigateToDetail,
    )
}
