package com.kus.feature.my.ui

import GetSessionAvailabilityUseCase
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventEmitter
import com.kus.feature.my.ui.state.MyUiState
import com.kus.shared.domain.my.usecase.GetMyInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyViewModel(
    private val getSessionAvailabilityUseCase: GetSessionAvailabilityUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val sessionEvents: SessionEventEmitter,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState = _uiState.asStateFlow()

    init {
        requireLogin()
    }

    fun requireLogin() = viewModelScope.launch {
        if (!getSessionAvailabilityUseCase()) {
            sessionEvents.emit(SessionEvent.LoginRequired)
            _uiState.update { it.copy(userProfileState = UiState.Idle) }
        }
        loadMyInfo()
    }

    fun loadMyInfo() = viewModelScope.launch {
        runCatching { getMyInfoUseCase() }
            .onSuccess { myInfo ->
                _uiState.update { it.copy(userProfileState = UiState.Success(myInfo)) }
            }
            .onFailure {
                _uiState.update { it.copy(userProfileState = UiState.Idle) }
            }
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }
}
