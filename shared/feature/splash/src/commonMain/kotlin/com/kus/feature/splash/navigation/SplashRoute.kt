package com.kus.feature.splash.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.SideEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.feature.splash.ui.SplashDestination
import com.kus.feature.splash.ui.SplashScreen
import com.kus.feature.splash.ui.SplashViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashRoute(
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    onLoadingChanged: (Boolean) -> Unit = {},
    viewModel: SplashViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val destination = viewModel.destination.collectAsStateWithLifecycle().value

    SideEffect {
        onLoadingChanged(uiState.isLoading)
    }

    SplashScreen()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.HOME -> navigateToHome()
            SplashDestination.LOGIN -> navigateToLogin()
            null -> Unit
        }
    }
}