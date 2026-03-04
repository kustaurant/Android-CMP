package com.kus.feature.evaluate.navigation

import androidx.compose.runtime.Composable
import com.kus.feature.evaluate.ui.EvaluateScreen

@Composable
fun EvaluateRoute(
    onBackClick: () -> Unit,
) {
    EvaluateScreen(
        onBackClick = onBackClick,
    )
}
