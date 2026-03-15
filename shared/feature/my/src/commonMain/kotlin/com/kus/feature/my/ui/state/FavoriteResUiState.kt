package com.kus.feature.my.ui.state

import UiState
import com.kus.shared.domain.model.my.FavoriteResItem

data class FavoriteResUiState(
    val restaurants: UiState<List<FavoriteResItem>> = UiState.Loading,
)
