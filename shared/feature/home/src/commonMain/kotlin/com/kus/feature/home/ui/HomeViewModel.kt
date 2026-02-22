package com.kus.feature.home.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import com.kus.feature.home.state.HomeUiState
import com.kus.shared.domain.home.usecase.GetHomeInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val getHomeInfoUseCase: GetHomeInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun getHomeInfo() {
        runCatching {
            getHomeInfoUseCase()
        }.onSuccess { homeInfo ->
            _uiState.update {
                it.copy(homeInfo = UiState.Success(homeInfo))
            }
        }.onFailure {
            _uiState.update {
                it.copy(homeInfo = UiState.Failure(UiError.Network))
            }
        }
    }
}
