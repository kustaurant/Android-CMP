package com.kus.feature.my.state

import UiState
import com.kus.shared.domain.model.my.MyCommentItem

data class MyCommentUiState(
    val comments: UiState<List<MyCommentItem>> = UiState.Loading,
)
