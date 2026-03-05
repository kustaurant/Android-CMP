package com.kus.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import kustaurant.shared.feature.home.generated.resources.Res
import kustaurant.shared.feature.home.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun KusSearchBoxWithNoAction(
    modifier: Modifier = Modifier,
    placeholder: String = "식당을 검색해볼까요?",
    onSearchBoxClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .noRippleClickable(onSearchBoxClick)
                .border(
                    width = 1.dp,
                    color = KusTheme.colors.c_43AB38,
                    shape = RoundedCornerShape(30.dp),
                )
                .background(
                    color = KusTheme.colors.c_FFFFFF,
                    shape = RoundedCornerShape(30.dp),
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                BasicTextField(
                    value = "",
                    onValueChange = { },
                    singleLine = true,
                    enabled = false,
                    modifier = Modifier.weight(1f),
                    textStyle = KusTheme.typography.type14r.copy(
                        color = KusTheme.colors.c_323232
                    ),
                    decorationBox = {
                        Text(
                            text = placeholder,
                            style = KusTheme.typography.type14r,
                            color = KusTheme.colors.c_AAAAAA,
                        )
                    },
                )

                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = "검색 아이콘",
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 4.dp),
                    tint = KusTheme.colors.c_43AB38,
                )
            }
        }
    }
}
