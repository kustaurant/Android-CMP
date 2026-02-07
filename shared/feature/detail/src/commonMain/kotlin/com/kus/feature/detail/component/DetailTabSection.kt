package com.kus.feature.detail.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@Composable
fun DetailTabSection(
    reviewCount: Int,
    menuContent: @Composable () -> Unit,
    reviewContent: @Composable () -> Unit,
    onReviewTabSelected: () -> Unit = {},
) {
    val reviewCountText = if (reviewCount > 999) "999+" else reviewCount.toString()
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(color = KusTheme.colors.c_FFFFFF)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(3.dp)
                .background(KusTheme.colors.c_E0E0E0)
        )

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val tabWidth = maxWidth / 2
            val indicatorX by animateDpAsState(
                targetValue = if (selectedIndex == 0) 0.dp else tabWidth,
                label = "detail_tab_indicator_x"
            )

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DetailTabItem(
                        title = "메뉴",
                        selected = selectedIndex == 0,
                        onClick = { selectedIndex = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    DetailTabItem(
                        title = "리뷰($reviewCountText)",
                        selected = selectedIndex == 1,
                        onClick = {
                            selectedIndex = 1
                            onReviewTabSelected()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(2.dp)
                        .background(KusTheme.colors.c_EAEAEA)
                ) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(tabWidth)
                            .offset(x = indicatorX)
                            .background(KusTheme.colors.c_43AB38)
                    )
                }
            }
        }

        Crossfade(
            targetState = selectedIndex,
            label = "detail_tab_content"
        ) { index ->
            when (index) {
                0 -> menuContent()
                1 -> reviewContent()
            }
        }
    }
}

@Composable
private fun DetailTabItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (selected) KusTheme.colors.c_323232 else KusTheme.colors.c_9BA5B0
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = KusTheme.typography.type14r.copy(color = color)
        )
    }
}
