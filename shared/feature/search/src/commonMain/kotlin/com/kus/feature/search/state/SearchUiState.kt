package com.kus.feature.search.state

import UiState
import com.kus.shared.domain.model.search.ResultItem

data class SearchUiState(
    val uiState: UiState<Unit> = UiState.Idle,
    val items: List<ResultItem> = emptyList(),
    val page: Int = 1,
    val isLastPage: Boolean = false,
    val isPaging: Boolean = false,
)
