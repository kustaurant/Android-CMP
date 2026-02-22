package com.kus.feature.search.state

import UiState
import com.kus.shared.domain.model.restaurant.RestaurantItem

data class SearchUiState(
    val results: UiState<List<RestaurantItem>> = UiState.Idle,
)