package com.kus.feature.login.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.feature.login.model.SocialAuthResult
import com.kus.feature.login.ui.LoginPhase
import com.kus.feature.login.ui.LoginScreen
import com.kus.feature.login.ui.LoginViewModel
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.oauth.util.NidOAuthCallback
import com.navercorp.nid.profile.domain.vo.NidProfile
import com.navercorp.nid.profile.util.NidProfileCallback
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
actual fun LoginRoute(
    navigateToHome: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.phase) {
        if (state.phase == LoginPhase.Success) {
            navigateToHome()
        }
    }

    LoginScreen(
        onNaverClick = {
            viewModel.beginNaverLogin()

            NidOAuth.requestLogin(activity, object : NidOAuthCallback {
                override fun onSuccess() {
                    NidOAuth.getUserProfile(object : NidProfileCallback<NidProfile> {
                        override fun onSuccess(result: NidProfile) {
                            val providerId = result.profile.id
                            val token = NidOAuth.getAccessToken().orEmpty()

                            if (providerId.isBlank() || token.isBlank()) {
                                viewModel.onNaverAuthFail(
                                    SocialAuthResult.Failure(
                                        code = "INVALID_DATA",
                                        message = "providerId or token empty"
                                    )
                                )
                                return
                            }

                            viewModel.onNaverAuthReady(providerId, token)
                        }

                        override fun onFailure(errorCode: String, errorDesc: String) {
                            viewModel.onNaverAuthFail(
                                SocialAuthResult.Failure(errorCode, errorDesc)
                            )
                        }
                    })
                }

                override fun onFailure(errorCode: String, errorDesc: String) {
                    val cancelled = errorDesc.contains("cancel", ignoreCase = true)
                    viewModel.onNaverAuthFail(
                        if (cancelled) SocialAuthResult.Cancelled(errorDesc)
                        else SocialAuthResult.Failure(errorCode, errorDesc)
                    )
                }
            })
        },
        onNavigateToHome = navigateToHome,
        onSkipLogin = {viewModel.deleteUserTokens()}
    )
}
