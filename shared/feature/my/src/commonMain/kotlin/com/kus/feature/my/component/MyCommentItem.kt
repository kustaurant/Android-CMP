package com.kus.feature.my.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.shared.domain.model.my.MyCommentItem
import kustaurant.shared.core.designsystem.generated.resources.ic_like
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
internal fun MyCommentItem(
    comment: MyCommentItem,
    onItemClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .noRippleClickable(onItemClick)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            text = "[ ${comment.postTitle} ]",
            style = KusTheme.typography.type14b,
            color = KusTheme.colors.c_AAAAAA,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(min = 32.dp),
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = comment.body,
            style = KusTheme.typography.type14r,
            color = KusTheme.colors.c_323232,
            modifier = Modifier.padding(end = 16.dp, bottom = 8.dp)
        )

        Spacer(Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReactionIconWithCount(
                painter = painterResource(CoreRes.drawable.ic_like),
                count = comment.likeCount,
                selected = false,
                activeColor = KusTheme.colors.c_43AB38,
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "|",
                style = KusTheme.typography.type12r,
                color = KusTheme.colors.c_E0E0E0
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = comment.timeAgo,
                style = KusTheme.typography.type12r,
                color = KusTheme.colors.c_AAAAAA
            )
        }

        Spacer(Modifier.height(10.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = KusTheme.colors.c_E0E0E0,
        )
    }
}

@Composable
private fun ReactionIconWithCount(
    painter: Painter,
    count: Int,
    selected: Boolean,
    activeColor : Color,
) {
    val tint = if (selected) activeColor else KusTheme.colors.c_AAAAAA
    val textColor = if (selected) activeColor else KusTheme.colors.c_AAAAAA

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = "아이콘 입니다.",
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = count.toString(),
            style = KusTheme.typography.type12r,
            color = textColor
        )
    }
}
