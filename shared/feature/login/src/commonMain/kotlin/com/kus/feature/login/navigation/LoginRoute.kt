package com.kus.feature.login.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun LoginRoute(
    navigateToHome: () -> Unit,
)
