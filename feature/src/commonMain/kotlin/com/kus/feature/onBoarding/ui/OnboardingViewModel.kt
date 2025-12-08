package com.kus.feature.onBoarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.firstLaunch.usecase.PostFirstLaunchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val postFirstLaunchUseCase: PostFirstLaunchUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    companion object {
        const val TOTAL_PAGE = 5
        const val LAST_INDEX = TOTAL_PAGE - 1
    }

    fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            OnboardingUiEvent.NextClicked -> {
                val next = _uiState.value.currentPage + 1
                if (next < TOTAL_PAGE) {
                    _uiState.value = _uiState.value.copy(currentPage = next)
                }
            }

            OnboardingUiEvent.GetStartedClicked -> {
                viewModelScope.launch {
                    postFirstLaunchUseCase()
                }
            }
        }
    }
}