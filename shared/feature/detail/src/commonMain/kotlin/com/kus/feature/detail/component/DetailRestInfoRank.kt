package com.kus.feature.detail.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@Composable
fun DetailRestInfoRank(
    mainTier: Int,
    isTempTier: Boolean = false,
) {
    val outerSize = 40.dp
    val borderInnerSize = 38.dp
    val innerSize = 36.dp
    val (startColor, endColor) = when (mainTier) {
        1 -> KusTheme.colors.c_0093FF to KusTheme.colors.c_C24BFF
        2 -> KusTheme.colors.c_01BAA6 to KusTheme.colors.c_1C64CA
        3 -> KusTheme.colors.c_FFB900 to KusTheme.colors.c_A31597
        else -> KusTheme.colors.c_9BA5B0 to KusTheme.colors.c_41454A
    }
    val infiniteTransition = rememberInfiniteTransition()
    val density = LocalDensity.current
    val outerSizePx = with(density) { outerSize.toPx() }
    val innerSizePx = with(density) { innerSize.toPx() }
    val animatedShift by infiniteTransition.animateFloat(
        initialValue = -outerSizePx * 0.35f,
        targetValue = outerSizePx * 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val gradientColors = listOf(startColor, if (isTempTier) endColor else startColor)
    val outerShape = RoundedCornerShape(100.dp)

    Box(
        modifier = Modifier.padding(top = if (isTempTier) 2.dp else 14.dp)
            .size(outerSize)
            .background(
                brush = rankBrush(
                    colors = gradientColors,
                    sizePx = outerSizePx,
                    animatedShift = animatedShift,
                    isAnimated = isTempTier
                ),
                shape = outerShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(borderInnerSize)
                .background(
                    color = KusTheme.colors.c_FFFFFF,
                    shape = outerShape
                )
        )

        Box(
            modifier = Modifier
                .size(innerSize)
                .background(
                    brush = rankBrush(
                        colors = gradientColors,
                        sizePx = innerSizePx,
                        animatedShift = animatedShift,
                        isAnimated = isTempTier
                    ),
                    shape = outerShape
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

private fun rankBrush(
    colors: List<Color>,
    sizePx: Float,
    animatedShift: Float,
    isAnimated: Boolean,
): Brush {
    return if (isAnimated) {
        Brush.linearGradient(
            colors = colors,
            start = Offset(x = 0f, y = animatedShift),
            end = Offset(x = 0f, y = sizePx + animatedShift)
        )
    } else {
        Brush.verticalGradient(colors = colors)
    }
}
