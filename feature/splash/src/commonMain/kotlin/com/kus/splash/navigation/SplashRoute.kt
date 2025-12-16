package com.kus.splash.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.splash.ui.SplashDestination
import com.kus.splash.ui.SplashScreen
import com.kus.splash.ui.SplashViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashRoute(
    navigateToOnboarding: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    SplashScreen()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.ONBOARDING -> navigateToOnboarding()
            SplashDestination.LOGIN -> navigateToLogin()
            null -> Unit
        }
    }
}