package com.kus.designsystem.navigation

import BottomBarItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.C_43AB38
import com.kus.designsystem.theme.C_AAAAAA
import com.kus.designsystem.theme.C_EAEAEA
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable

@Composable
fun BottomBar(
    items: List<BottomBarItem>,
    selectedKey: String,
    onItemClick: (key: String) -> Unit,
    modifier: Modifier = Modifier,
    selectedTint: Color = C_43AB38,
    labelUnselected: Color = C_AAAAAA,
) {
    Column(modifier.background(KusTheme.colors.c_FFFFFF)) {
        Surface(color = C_EAEAEA) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                    .padding(horizontal = 17.dp)
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    val selected = item.key == selectedKey

                    Column(
                        modifier = Modifier.weight(1f)
                            .noRippleClickable { onItemClick(item.key) }
                            .padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(Modifier.size(26.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                painter = item.icon,
                                contentDescription = item.title,
                                tint = if (selected) selectedTint else Color.Unspecified,
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = item.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selected) selectedTint else labelUnselected,
                        )
                    }
                }
            }
        }
    }
}