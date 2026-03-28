package com.kus.feature.tier.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.feature.tier.ui.TierFilterState
import com.kus.feature.tier.ui.TierScreen
import com.kus.feature.tier.ui.TierViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TierRoute(
    navigateToTierCategorySelect: (TierFilterState) -> Unit,
    initialFilter: TierFilterState?,
    navigateToDetail: (Long, Boolean) -> Unit,
    resultFilter: TierFilterState?,
    consumeInitial: () -> Unit,
    consumeResult: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel: TierViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(initialFilter, resultFilter) {
        when {
            resultFilter != null -> {
                viewModel.setCategory(
                    cuisines = resultFilter.cuisines,
                    situations = resultFilter.situations,
                    locations = resultFilter.locations,
                )
                consumeResult()
                consumeInitial()
            }

            initialFilter != null -> {
                viewModel.setCategory(
                    cuisines = initialFilter.cuisines,
                    situations = initialFilter.situations,
                    locations = initialFilter.locations,
                )
                consumeInitial()
            }
        }
    }

    TierScreen(
        onFilterClick = { navigateToTierCategorySelect(uiState.filterState) },
        onShowMessage = onShowMessage,
        onNavigateRestaurantDetail = navigateToDetail,
    )
}
