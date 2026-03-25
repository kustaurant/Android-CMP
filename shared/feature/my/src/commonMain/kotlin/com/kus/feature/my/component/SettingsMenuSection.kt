package com.kus.feature.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.type.MenuItemUi
import org.jetbrains.compose.resources.painterResource
import kotlin.enums.EnumEntries

@Composable
internal fun <T> MenuList(
    title: String,
    items: EnumEntries<T>,
    onClick: (T) -> Unit,
) where T : Enum<T>, T : MenuItemUi {
    Column(
        modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp),
    ) {
        Text(
            text = title,
            style = KusTheme.typography.type14sb,
            color = KusTheme.colors.c_AAAAAA,
            modifier = Modifier.padding(start = 16.dp),
        )

        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .background(KusTheme.colors.c_FFFFFF, RoundedCornerShape(12.dp)),

            ) {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(item) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = item.title,
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = item.title,
                        style = KusTheme.typography.type14r
                    )
                }

                Spacer(Modifier.width(12.dp))

                if (index != items.lastIndex) HorizontalDivider(
                    color = KusTheme.colors.c_F3F3F3,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
    }
}
