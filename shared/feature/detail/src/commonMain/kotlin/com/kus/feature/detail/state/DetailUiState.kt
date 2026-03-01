package com.kus.feature.detail.state

import UiState
import com.kus.shared.domain.model.detail.RestaurantDetail

data class DetailUiState(
    val restaurant: UiState<RestaurantDetail> = UiState.Loading,
)
