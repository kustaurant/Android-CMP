package com.kus.splash.ui

data class SplashUiState(
    val isLoading : Boolean = true,
    val isFirstLaunch : Boolean? = null
)

enum class SplashDestination {
    ONBOARDING,
    LOGIN,
}