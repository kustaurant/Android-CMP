package com.kus.feature.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.shared.core.designsystem.generated.resources.ic_location
import kustaurant.shared.feature.detail.generated.resources.ic_file_check
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes
import kustaurant.shared.feature.detail.generated.resources.Res as DetailRes

@Composable
fun DetailRestInfoMore(
    title: String,
    content: String,
    enableSeeMore: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val isPartnerInfo = title == "제휴정보"
    val iconRes = if (isPartnerInfo) {
        DetailRes.drawable.ic_file_check
    } else {
        CoreRes.drawable.ic_location
    }
    val titleStyle =
        if (isPartnerInfo) KusTheme.typography.type14m.copy(color = KusTheme.colors.c_AAAAAA)
        else KusTheme.typography.type14m.copy(color = KusTheme.colors.c_666666)
    val seeMoreStyle = SpanStyle(
        color = KusTheme.colors.c_AAAAAA,
        textDecoration = TextDecoration.Underline
    )
    val actualContent = if (content == "" && isPartnerInfo) "해당사항 없음" else content

    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(iconRes),
            modifier = Modifier.size(16.dp)
                .padding(top = 1.dp),
            contentDescription = null,
        )

        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = title,
                style = KusTheme.typography.type14b.copy(
                    color = KusTheme.colors.c_000000
                )
            )

            ExpandableSeeMoreText(
                text = actualContent,
                textStyle = titleStyle,
                moreTextStyle = seeMoreStyle,
                isExpanded = isExpanded,
                enableSeeMore = enableSeeMore,
                ellipsis = "… ",
                onExpandedChange = { isExpanded = it },
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}
