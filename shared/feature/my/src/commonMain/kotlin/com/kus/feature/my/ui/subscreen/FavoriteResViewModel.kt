package com.kus.feature.my.ui.subscreen

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.ui.state.FavoriteResUiState
import com.kus.shared.domain.my.usecase.GetFavoriteResUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteResViewModel(
    private val getFavoriteResUseCase: GetFavoriteResUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoriteResUiState())
    val uiState = _uiState.asStateFlow()

    fun getFavorites() = viewModelScope.launch {
        runCatching { getFavoriteResUseCase() }
            .onSuccess { result ->
                _uiState.update { it.copy(restaurants = UiState.Success(result)) }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(restaurants = UiState.Failure(UiError.Network))
                }
            }
    }
}
