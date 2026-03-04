package com.kus.feature.evaluate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@Composable
fun EvaluationRestInfoRank(
    mainTier: Int,
) {
    val rankColor: Color = when (mainTier) {
        1 -> KusTheme.colors.c_0093FF
        2 -> KusTheme.colors.c_01BAA6
        3 -> KusTheme.colors.c_FFB900
        else -> KusTheme.colors.c_9BA5B0
    }

    Box(
        modifier = Modifier.size(40.dp)
            .background(
                color = KusTheme.colors.c_FFFFFF,
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                width = 1.dp,
                color = rankColor,
                shape = RoundedCornerShape(100.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(36.dp)
                .background(
                    color = rankColor,
                    shape = RoundedCornerShape(100.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mainTier.toString(),
                style = KusTheme.typography.type26sb.copy(
                    color = KusTheme.colors.c_FFFFFF
                )
            )
        }
    }
}
