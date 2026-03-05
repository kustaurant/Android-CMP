package com.kus.feature.detail.navigation

import androidx.compose.runtime.Composable
import com.kus.feature.detail.ui.DetailScreen

@Composable
fun DetailRoute(
    navigateToEvaluate: () -> Unit,
    onBackClick: () -> Unit,
) {
    DetailScreen(
        navigateToEvaluate = navigateToEvaluate,
        onBackClick = onBackClick,
    )
}
