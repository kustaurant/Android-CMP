package com.kus.feature.login.ui

import UiState
import com.kus.domain.auth.model.AuthToken

// commonMain

enum class LoginPhase { Idle, Authorizing, Exchanging, Success }

data class LoginUiState(
    val phase: LoginPhase = LoginPhase.Idle,
    val isLoginButtonEnabled: Boolean = true,
    val authState: UiState<Unit> = UiState.Success(Unit),
    val serverState: UiState<AuthToken> = UiState.Idle,
)
