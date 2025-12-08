package com.kus.feature.login.navigation

import androidx.compose.runtime.Composable
import com.kus.feature.login.ui.LoginScreen

@Composable
fun LoginRoute(
    navigateToHome: () -> Unit,
) {
    LoginScreen()
}