package com.kus.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.firstLaunch.usecase.GetFirstLaunchUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getFirstLaunchUseCase: GetFirstLaunchUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            val minDelay = async {
                delay(650)
            }

            val firstLaunch = async {
                getFirstLaunchUseCase().first()
            }

            minDelay.await()
            val isFirst = firstLaunch.await()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isFirstLaunch = isFirst
                )
            }

            _destination.value =
                if (isFirst) SplashDestination.ONBOARDING
                else SplashDestination.LOGIN
        }
    }
}
