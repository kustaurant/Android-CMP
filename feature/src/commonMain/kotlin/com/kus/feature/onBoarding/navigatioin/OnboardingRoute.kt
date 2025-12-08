package com.kus.feature.onBoarding.navigatioin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.feature.onBoarding.ui.OnboardingScreen
import com.kus.feature.onBoarding.ui.OnboardingUiEvent
import com.kus.feature.onBoarding.ui.OnboardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingRoute(
    navigateToLogin: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingScreen(
        state = uiState,
        onNextClick = { viewModel.onEvent(OnboardingUiEvent.NextClicked) },
        onGetStartedClick = {
            viewModel.onEvent(OnboardingUiEvent.GetStartedClicked)
            navigateToLogin()
        }
    )
}