package com.kus.feature.community.ui.write

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@Composable
fun KusTopBarSlots(
    modifier: Modifier = Modifier,
    left: @Composable (() -> Unit)? = null,
    right: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var leftWidthPx by remember { mutableIntStateOf(0) }
    var rightWidthPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current
    val leftWidthDp = with(density) { leftWidthPx.toDp() }
    val rightWidthDp = with(density) { rightWidthPx.toDp() }

    val safeSidePadding = maxOf(leftWidthDp, rightWidthDp)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = safeSidePadding),
                contentAlignment = Alignment.Center
            ) {
                content()
            }

            if (left != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .onSizeChanged { leftWidthPx = it.width }
                ) {
                    left()
                }
            } else {
                LaunchedEffect(Unit) { leftWidthPx = 0 }
            }

            if (right != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .onSizeChanged { rightWidthPx = it.width },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    content = right
                )
            } else {
                LaunchedEffect(Unit) { rightWidthPx = 0 }
            }
        }

        HorizontalDivider(thickness = 1.5.dp, color = KusTheme.colors.c_EAEAEA)
    }
}