package com.kus.feature.my.ui.state

import UiState
import com.kus.shared.domain.model.my.EvaluatedResItem

data class CheckedUiState(
    val restaurants: UiState<List<EvaluatedResItem>> = UiState.Loading,
)
