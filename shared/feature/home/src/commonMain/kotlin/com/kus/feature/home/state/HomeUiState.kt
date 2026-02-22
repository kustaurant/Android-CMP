package com.kus.feature.home.state

import UiState
import com.kus.shared.domain.model.home.HomeInfo

data class HomeUiState(
    val homeInfo: UiState<HomeInfo> = UiState.Loading,
)
