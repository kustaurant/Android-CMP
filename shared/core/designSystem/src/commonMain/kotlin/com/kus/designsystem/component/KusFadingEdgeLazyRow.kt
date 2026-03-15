package com.kus.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun KusFadingEdgeLazyRow(
    state: LazyListState,
    modifier: Modifier = Modifier,
    fadeWidth: Dp = 16.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit,
) {
    val showLeftFade by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 0 || state.firstVisibleItemScrollOffset > 0
        }
    }
    val showRightFade by remember {
        derivedStateOf {
            val info = state.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            val totalCount = info.totalItemsCount
            if (totalCount == 0) return@derivedStateOf false

            val isLastItemVisible = lastVisible.index == totalCount - 1
            if (!isLastItemVisible) return@derivedStateOf true

            val viewportEnd = info.viewportEndOffset
            val lastItemEnd = lastVisible.offset + lastVisible.size
            lastItemEnd > viewportEnd
        }
    }

    val fadePx = with(LocalDensity.current) { fadeWidth.toPx() }

    LazyRow(
        state = state,
        modifier = modifier
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                val w = size.width

                val left = if (showLeftFade) fadePx else 0f
                val right = if (showRightFade) fadePx else 0f

                if (left == 0f && right == 0f) return@drawWithContent

                val brush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0.0f to Color.Transparent,
                        (left / w).coerceIn(0f, 1f) to Color.Black,
                        ((w - right) / w).coerceIn(0f, 1f) to Color.Black,
                        1.0f to Color.Transparent
                    ),
                    startX = 0f,
                    endX = w
                )

                drawRect(
                    brush = brush,
                    topLeft = Offset.Zero,
                    size = size,
                    blendMode = BlendMode.DstIn
                )
            },
        horizontalArrangement = horizontalArrangement,
        contentPadding = contentPadding,
        content = content
    )
}