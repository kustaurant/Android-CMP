package com.kus.feature.search.state

import UiState
import com.kus.shared.domain.model.search.SearchResult

data class SearchUiState(
    val results: UiState<SearchResult> = UiState.Idle,
)