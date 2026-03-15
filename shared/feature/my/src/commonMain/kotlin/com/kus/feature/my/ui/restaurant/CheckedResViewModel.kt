package com.kus.feature.my.ui.restaurant

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.ui.state.CheckedUiState
import com.kus.shared.domain.my.usecase.GetEvaluatedResUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckedResViewModel(
    private val getEvaluatedResUseCase: GetEvaluatedResUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckedUiState())
    val uiState = _uiState.asStateFlow()

    fun getEvaluatedRes() = viewModelScope.launch {
        runCatching { getEvaluatedResUseCase() }
            .onSuccess { result ->
                _uiState.update { it.copy(restaurants = UiState.Success(result)) }
            }
            .onFailure { error ->
                _uiState.update { it.copy(restaurants = UiState.Failure(UiError.Network)) }
            }
    }
}