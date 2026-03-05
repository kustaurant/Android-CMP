package com.kus.feature.home.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.home.state.HomeUiState
import com.kus.shared.domain.home.usecase.GetHomeInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeInfoUseCase: GetHomeInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun getHomeInfo() = viewModelScope.launch {
        runCatching {
            getHomeInfoUseCase()
        }.onSuccess { homeInfo ->
            _uiState.update { it.copy(homeInfo = UiState.Success(homeInfo)) }
        }.onFailure {
            _uiState.update { it.copy(homeInfo = UiState.Failure(UiError.Network)) }
        }
    }
}
