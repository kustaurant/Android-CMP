package com.kus.feature.my.ui.community

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.ui.state.MyArticleUiState
import com.kus.shared.domain.my.usecase.GetMyPostsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyArticleViewModel(
    private val getMyPostsUseCase: GetMyPostsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyArticleUiState())
    val uiState = _uiState.asStateFlow()

    fun getMyArticle() = viewModelScope.launch {
        runCatching { getMyPostsUseCase() }
            .onSuccess { result ->
                _uiState.update { it.copy(articles = UiState.Success(result)) }
            }
            .onFailure {
                _uiState.update {
                    it.copy(articles = UiState.Failure(UiError.Network))
                }
            }
    }
}