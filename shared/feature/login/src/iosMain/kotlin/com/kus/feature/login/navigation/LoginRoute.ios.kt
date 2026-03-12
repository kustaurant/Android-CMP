package com.kus.feature.login.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.feature.login.SocialAuthClient
import com.kus.feature.login.model.NaverAuthResult
import com.kus.feature.login.model.SocialAuthResult
import com.kus.feature.login.ui.LoginPhase
import com.kus.feature.login.ui.LoginScreen
import com.kus.feature.login.ui.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun LoginRoute(
    navigateToHome: () -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val authClient: SocialAuthClient = koinInject()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.phase) {
        if (state.phase == LoginPhase.Success) {
            navigateToHome()
        }
    }

    val scope = rememberCoroutineScope()

    LoginScreen(
        onNaverClick = {
            viewModel.beginNaverLogin()

            scope.launch {
                when (val res = authClient.login()) {
                    is NaverAuthResult.Success ->
                        viewModel.onNaverAuthReady(
                            providerId = res.payload.providerId,
                            naverAccessToken = res.payload.accessToken
                        )

                    is NaverAuthResult.Cancelled ->
                        viewModel.onNaverAuthFail(
                            SocialAuthResult.Failure("CANCELLED", res.reason)
                        )

                    is NaverAuthResult.Failure -> {
                        viewModel.onNaverAuthFail(
                            SocialAuthResult.Failure(res.code, res.message)
                        )
                    }
                }
            }
        },
        onNavigateToHome = navigateToHome,
        onSkipLogin = { viewModel.deleteUserInfo() }
    )
}
