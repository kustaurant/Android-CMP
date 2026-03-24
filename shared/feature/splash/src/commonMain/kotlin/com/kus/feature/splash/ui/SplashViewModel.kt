package com.kus.feature.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.auth.usecase.GetSessionAvailabilityUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getSessionAvailabilityUseCase: GetSessionAvailabilityUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        viewModelScope.launch {
            delay(650)
            val isLoggedIn = runCatching { getSessionAvailabilityUseCase() }
                .getOrElse { false }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    hasSession = isLoggedIn,
                )
            }

            _destination.value = if (isLoggedIn) SplashDestination.HOME else SplashDestination.LOGIN
        }
    }
}
