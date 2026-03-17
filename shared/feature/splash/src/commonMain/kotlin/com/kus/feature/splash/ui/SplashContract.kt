package com.kus.feature.splash.ui

data class SplashUiState(
    val isLoading : Boolean = true,
    val hasSession: Boolean = false,
)

enum class SplashDestination {
    HOME,
    LOGIN,
}