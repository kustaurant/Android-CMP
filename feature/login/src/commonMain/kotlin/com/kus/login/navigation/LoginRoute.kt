package com.kus.login.navigation

import androidx.compose.runtime.Composable
import com.kus.login.ui.LoginScreen

@Composable
fun LoginRoute(
    navigateToHome: () -> Unit,
) {
    LoginScreen()
}