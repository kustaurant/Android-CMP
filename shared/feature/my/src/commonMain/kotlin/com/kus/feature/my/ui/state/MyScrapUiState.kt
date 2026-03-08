package com.kus.feature.my.ui.state

import UiState
import com.kus.shared.domain.model.my.MyPostItem

data class MyScrapUiState(
    val articles: UiState<List<MyPostItem>> = UiState.Loading,
)
