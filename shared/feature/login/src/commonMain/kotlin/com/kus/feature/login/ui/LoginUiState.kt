package com.kus.feature.login.ui

// commonMain

enum class LoginPhase { Idle, Authorizing, Exchanging, Success }

data class LoginUiState(
    val phase: LoginPhase = LoginPhase.Idle,

    // 화면에서 쓰기 좋은 값들
    val isLoginButtonEnabled: Boolean = true,
    val lastProvider: String? = null,

    // 네이버 SDK/브라우저 인증 결과(액세스 토큰 등)
    val authState: UiState<NaverAuthData> = UiState.Success(NaverAuthData.Empty),

    // 서버 로그인(우리 서비스 토큰 발급) 결과
    val serverState: UiState<AuthTokens> = UiState.Loading,
)
