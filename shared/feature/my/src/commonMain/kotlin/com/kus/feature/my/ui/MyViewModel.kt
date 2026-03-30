package com.kus.feature.my.ui

import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventEmitter
import com.kus.domain.auth.usecase.DeleteUserInfoUseCase
import com.kus.domain.auth.usecase.GetSessionAvailabilityUseCase
import com.kus.domain.auth.usecase.LogoutUseCase
import com.kus.feature.my.event.MyNavigationEvent
import com.kus.feature.my.state.MyUiState
import com.kus.shared.domain.my.usecase.GetMyInfoUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyViewModel(
    private val getSessionAvailabilityUseCase: GetSessionAvailabilityUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val sessionEvents: SessionEventEmitter,
    private val logoutUseCase: LogoutUseCase,
    private val deleteUserInfoUseCase: DeleteUserInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<MyNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun requireLogin() = viewModelScope.launch {
        if (!getSessionAvailabilityUseCase()) {
            sessionEvents.emit(SessionEvent.LoginRequired)
            _uiState.update { it.copy(userProfileState = UiState.Idle) }
        } else {
            loadMyInfo()
        }
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

    fun logout() = viewModelScope.launch {
        runCatching { logoutUseCase() }
            .onSuccess {
                _uiState.update {
                    it.copy(userProfileState = UiState.Idle, toastMessage = "로그아웃 되었습니다.")
                }
                _navigationEvent.emit(MyNavigationEvent.NavigateToLogin)
            }
            .onFailure {
                _uiState.update { it.copy(toastMessage = "요청에 실패하였습니다.") }
            }
    }

    fun deleteAccount() = viewModelScope.launch {
        runCatching { deleteUserInfoUseCase() }
            .onSuccess {
                _uiState.update {
                    it.copy(userProfileState = UiState.Idle, toastMessage = "계정이 삭제되었습니다.")
                }
                _navigationEvent.emit(MyNavigationEvent.NavigateToLogin)
            }
            .onFailure {
                _uiState.update { it.copy(toastMessage = "요청에 실패하였습니다.") }
            }
    }

    fun clearToastMessage() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }
}
