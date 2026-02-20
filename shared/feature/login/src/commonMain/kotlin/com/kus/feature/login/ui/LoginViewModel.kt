package com.kus.feature.login.ui

import UiError
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.auth.usecase.PostNaverLoginUseCase
import com.kus.feature.login.model.SocialAuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val postNaverLoginUseCase: PostNaverLoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        LoginUiState(
            authState = UiState.Idle
        )
    )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun beginNaverLogin() {
        val s = _uiState.value
        if (s.phase == LoginPhase.Authorizing || s.phase == LoginPhase.Exchanging) return

        _uiState.update {
            it.copy(
                phase = LoginPhase.Authorizing,
                isLoginButtonEnabled = false,
                authState = UiState.Loading,
                serverState = UiState.Idle,
            )
        }
    }

    fun onNaverAuthFail(auth: SocialAuthResult) {
        if(auth !is SocialAuthResult.Failure) return

        _uiState.update {
            it.copy(
                phase = LoginPhase.Idle,
                isLoginButtonEnabled = true,
                authState = UiState.Failure(
                    UiError.Message("${auth.code}: ${auth.message ?: "네이버 로그인 실패"}")
                )
            )
        }
    }

    fun onNaverAuthReady(providerId: String, naverAccessToken: String) {
        val s = _uiState.value
        if (s.phase == LoginPhase.Exchanging || s.phase == LoginPhase.Success) return

        if (providerId.isBlank()) {
            _uiState.update {
                it.copy(
                    phase = LoginPhase.Idle,
                    isLoginButtonEnabled = true,
                    authState = UiState.Failure(UiError.Message("providerId가 비어있어요."))
                )
            }
            return
        }

        if (naverAccessToken.isBlank()) {
            _uiState.update {
                it.copy(
                    phase = LoginPhase.Idle,
                    isLoginButtonEnabled = true,
                    authState = UiState.Failure(UiError.Message("네이버 토큰이 비어있어요."))
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                phase = LoginPhase.Exchanging,
                serverState = UiState.Loading
            )
        }

        viewModelScope.launch {
            runCatching { postNaverLoginUseCase(providerId, naverAccessToken) }
                .onSuccess { tokens ->
                    _uiState.update {
                        it.copy(
                            phase = LoginPhase.Success,
                            isLoginButtonEnabled = true,
                            serverState = UiState.Success(tokens)
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            phase = LoginPhase.Idle,
                            isLoginButtonEnabled = true,
                            serverState = UiState.Failure(
                                e.message?.let { msg -> UiError.Message(msg) }
                                    ?: UiError.Message("서버 로그인 실패")
                            )
                        )
                    }
                }
        }
    }
}
