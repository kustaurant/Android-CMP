package com.kus.feature.detail.state

import UiState
import com.kus.feature.detail.model.ReviewSort
import com.kus.shared.domain.model.detail.RestaurantDetail
import com.kus.shared.domain.model.detail.RestaurantReview

data class DetailUiState(
    val restaurant: UiState<RestaurantDetail> = UiState.Loading,
    val reviews: UiState<List<RestaurantReview>> = UiState.Idle,
    val reviewSort: ReviewSort = ReviewSort.Popular,
    val toastMessage: String? = null,
)
