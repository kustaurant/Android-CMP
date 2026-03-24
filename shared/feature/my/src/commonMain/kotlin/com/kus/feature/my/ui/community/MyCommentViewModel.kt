package com.kus.feature.my.ui.community

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.state.MyCommentUiState
import com.kus.shared.domain.my.usecase.GetMyCommentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyCommentViewModel(
    private val getMyCommentsUseCase: GetMyCommentsUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(MyCommentUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMyComments()
    }

    fun getMyComments() = viewModelScope.launch {
        runCatching { getMyCommentsUseCase() }
            .onSuccess { result ->
                _uiState.update { it.copy(comments = UiState.Success(result)) }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(comments = UiState.Failure(UiError.Network))
                }
            }
    }
}