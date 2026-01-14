package com.kus.feature.onboarding.ui

data class OnboardingUiState(
    val currentPage: Int = 0,
)

sealed interface OnboardingUiEvent {
    data object NextClicked : OnboardingUiEvent
    data object GetStartedClicked : OnboardingUiEvent
}