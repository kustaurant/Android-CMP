package com.kus.feature.my.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.ui.type.MyTab

@Composable
internal fun MyTabRow(
    tabs: List<MyTab>,
    selectedIndex: Int,
    onTabClick: (Int) -> Unit,
) {
    val indicatorColor = KusTheme.colors.c_43AB38
    val density = LocalDensity.current

    val tabWidths = remember { mutableStateListOf<Int>() }

    Column {
        Row {
            tabs.forEachIndexed { index, tab ->

                val selected = selectedIndex == index

                Column(
                    modifier = Modifier
                        .onSizeChanged { size ->
                            if (tabWidths.size <= index) {
                                tabWidths.add(size.width)
                            } else {
                                tabWidths[index] = size.width
                            }
                        }
                        .selectable(
                            selected = selected,
                            onClick = { onTabClick(index) },
                            role = Role.Tab
                        )
                        .padding(horizontal = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val textColor by animateColorAsState(
                        targetValue = if (selected) Color.Black
                        else KusTheme.colors.c_AAAAAA,
                        label = "TabColor"
                    )

                    Text(
                        text = tab.title,
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = textColor,
                        style = KusTheme.typography.type16sb,
                    )
                }
            }
        }

        if (tabWidths.size == tabs.size) {

            val indicatorOffset by animateDpAsState(
                targetValue = with(density) {
                    tabWidths.take(selectedIndex).sum().toDp()
                },
                label = "IndicatorOffset"
            )

            val indicatorWidth by animateDpAsState(
                targetValue = with(density) {
                    tabWidths[selectedIndex].toDp()
                },
                label = "IndicatorWidth"
            )

            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(indicatorWidth)
                    .height(2.dp)
                    .background(indicatorColor, RoundedCornerShape(4.dp)),
            )
        }
    }
}

