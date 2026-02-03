package com.kus.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// commonMain
class LoginViewModel(
    private val naverAuthPlatform: NaverAuthPlatform,
    private val postNaverLoginUseCase: PostNaverLoginUseCase,
    private val prefs: AuthPreferenceDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState(
        authState = UiState.Success(NaverAuthData.Empty),
        serverState = UiState.Success(
            AuthTokens(accessToken = "", refreshToken = "", userId = "")
        ) // 초기값은 프로젝트 스타일에 맞게 바꿔도 됨
    ))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onNaverLoginClick(context: PlatformContext) {
        val s = _uiState.value
        if (s.phase == LoginPhase.Authorizing || s.phase == LoginPhase.Exchanging) return

        _uiState.update {
            it.copy(
                phase = LoginPhase.Authorizing,
                isLoginButtonEnabled = false,
                lastProvider = "NAVER",
                authState = UiState.Loading,
            )
        }

        viewModelScope.launch {
            when (val auth = naverAuthPlatform.authorize(context)) {
                is SocialAuthResult.Success -> {
                    val accessToken = auth.accessToken
                    _uiState.update {
                        it.copy(
                            phase = LoginPhase.Exchanging,
                            authState = UiState.Success(NaverAuthData(accessToken = accessToken)),
                            serverState = UiState.Loading
                        )
                    }
                    exchangeToServer(accessToken)
                }

                is SocialAuthResult.Cancelled -> {
                    _uiState.update {
                        it.copy(
                            phase = LoginPhase.Idle,
                            isLoginButtonEnabled = true,
                            authState = UiState.Failure(auth.message ?: "로그인을 취소했어요.")
                        )
                    }
                }

                is SocialAuthResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            phase = LoginPhase.Idle,
                            isLoginButtonEnabled = true,
                            authState = UiState.Failure("${auth.code}: ${auth.message ?: "네이버 로그인 실패"}")
                        )
                    }
                }
            }
        }
    }

    private suspend fun exchangeToServer(naverAccessToken: String) {
        runCatching {
            postNaverLoginUseCase(naverAccessToken)
        }.onSuccess { tokens ->
            // 네이티브 ViewModel에서 하던 prefs 저장과 동일
            prefs.setUserId(tokens.userId)
            prefs.setAccessToken(tokens.accessToken)
            prefs.setRefreshToken(tokens.refreshToken)

            _uiState.update {
                it.copy(
                    phase = LoginPhase.Success,
                    isLoginButtonEnabled = true,
                    serverState = UiState.Success(tokens)
                )
            }
        }.onFailure { e ->
            _uiState.update {
                it.copy(
                    phase = LoginPhase.Idle,
                    isLoginButtonEnabled = true,
                    serverState = UiState.Failure(e.message ?: "서버 로그인 실패")
                )
            }
        }
    }

    fun resetErrors() {
        _uiState.update {
            it.copy(
                authState = UiState.Success(NaverAuthData.Empty),
                // serverState는 유지할지/초기화할지 취향
            )
        }
    }
}
