package com.kus.feature.search.component

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import kustaurant.shared.feature.search.generated.resources.Res
import kustaurant.shared.feature.search.generated.resources.ic_search
import kustaurant.shared.feature.search.generated.resources.ic_x_circle
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun KusSearchBox(
    searchTerm: TextFieldValue,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    placeholder: String = "검색어를 입력하세요",
    onValueChange: (TextFieldValue) -> Unit,
    onSearchButonClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
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
                value = searchTerm,
                onValueChange = onValueChange,
                singleLine = true,
                enabled = true,
                modifier = textFieldModifier.weight(1f),
                textStyle = KusTheme.typography.type14r.copy(
                    color = KusTheme.colors.c_323232
                ),
                decorationBox = { innerTextField ->
                    if (searchTerm.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = KusTheme.typography.type14r,
                            color = KusTheme.colors.c_AAAAAA,
                        )
                    }
                    innerTextField()
                },
            )

            if (searchTerm.text.isNotEmpty()) {
                Icon(
                    painter = painterResource(Res.drawable.ic_x_circle),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 13.dp, end = 6.dp)
                        .size(20.dp)
                        .noRippleClickable { onValueChange(TextFieldValue("")) },
                    tint = Color.Unspecified,
                )
            }

            Icon(
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = "검색 아이콘",
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 4.dp)
                    .noRippleClickable(onSearchButonClick),
                tint = KusTheme.colors.c_43AB38,
            )
        }
    }
}
