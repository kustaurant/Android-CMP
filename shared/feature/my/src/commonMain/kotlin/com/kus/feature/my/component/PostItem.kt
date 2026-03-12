package com.kus.feature.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.shared.domain.model.my.MyPostItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.ic_comment
import kustaurant.shared.core.designsystem.generated.resources.ic_like
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
internal fun PostItem(
    item: MyPostItem,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(2.dp),
            color = KusTheme.colors.c_EAEAEA
        ) {
            Text(
                text = item.postCategory,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = KusTheme.typography.type11r,
                color = KusTheme.colors.c_AAAAAA
            )
        }

        Spacer(Modifier.height(8.dp))

        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.postTitle,
                    style = KusTheme.typography.type15sb,
                    color = KusTheme.colors.c_323232,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = item.body,
                    style = KusTheme.typography.type13r,
                    color = KusTheme.colors.c_323232,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(CoreRes.drawable.ic_like),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = item.likeCount.toString(),
                        style = KusTheme.typography.type12r,
                        color = KusTheme.colors.c_43AB38
                    )

                    Spacer(Modifier.width(10.dp))

                    Image(
                        painter = painterResource(CoreRes.drawable.ic_comment),
                        contentDescription = null,
                        modifier = Modifier.size(11.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = item.commentCount.toString(),
                        style = KusTheme.typography.type12r,
                        color = KusTheme.colors.c_AAAAAA
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "|",
                        color = KusTheme.colors.c_E0E0E0
                    )

                    Spacer(Modifier.width(2.dp))

                    Text(
                        text = item.timeAgo,
                        style = KusTheme.typography.type12r,
                        color = KusTheme.colors.c_AAAAAA
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            val thumbUrl = item.postImgUrl
            if (thumbUrl.isNotBlank()) {
                KamelImage(
                    resource = asyncPainterResource(thumbUrl),
                    contentDescription = "게시글 썸네일",
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = KusTheme.colors.c_E0E0E0,
        modifier = Modifier.padding(horizontal = 14.dp),
    )
}
