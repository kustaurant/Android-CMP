package com.kus.feature.my.ui.community

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.state.MyScrapUiState
import com.kus.shared.domain.my.usecase.GetMyScrapsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyScrapViewModel(
    private val getMyScrapsUseCase: GetMyScrapsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyScrapUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getScraps()
    }

    fun getScraps() = viewModelScope.launch {
        runCatching { getMyScrapsUseCase() }
            .onSuccess { result ->
                _uiState.update { it.copy(articles = UiState.Success(result)) }
            }
            .onFailure {
                _uiState.update { it.copy(articles = UiState.Failure(UiError.Network)) }
            }
    }
}
