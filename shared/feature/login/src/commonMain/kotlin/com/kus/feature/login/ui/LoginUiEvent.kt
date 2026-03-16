package com.kus.feature.login.ui

sealed class LoginUiEvent {
    object NavigateToHome : LoginUiEvent()
}