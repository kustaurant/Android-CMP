package com.kus.feature.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_kus_blank
import kustaurant.shared.core.designsystem.generated.resources.ic_location
import kustaurant.shared.core.designsystem.generated.resources.ic_rank_none
import kustaurant.shared.core.designsystem.generated.resources.ic_saved
import kustaurant.shared.core.designsystem.generated.resources.ic_tier_1
import kustaurant.shared.core.designsystem.generated.resources.ic_tier_2
import kustaurant.shared.core.designsystem.generated.resources.ic_tier_3
import kustaurant.shared.core.designsystem.generated.resources.ic_tier_4
import kustaurant.shared.core.designsystem.generated.resources.ic_unsaved
import org.jetbrains.compose.resources.painterResource

@Composable
fun KusFavoriteResThumbnail(
    modifier: Modifier = Modifier,
    tier: Int? = null,
    restName: String,
    restThumbnail: String? = null,
    location: String? = null,
    isSaved: Boolean,
    onClick: () -> Unit = {},
) {
    val locationText = location ?: "위치정보 없음"
    val savedModel = if (isSaved) Res.drawable.ic_saved else Res.drawable.ic_unsaved

    Row(
        modifier = modifier
            .background(
                color = KusTheme.colors.c_FFFFFF,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
            .noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(11.dp)),
        ) {
            if (restThumbnail.isNullOrBlank()) {
                Image(
                    painter = painterResource(Res.drawable.ic_kus_blank),
                    contentDescription = "음식점 사진이 없을 시, 표시되는 사진입니다.",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
                )
            } else {
                KamelImage(
                    resource = asyncPainterResource(restThumbnail),
                    contentDescription = "음식점 사진입니다.",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp),
                    onFailure = {
                        Image(
                            painter = painterResource(Res.drawable.ic_kus_blank),
                            contentDescription = "음식점 사진이 없을 시, 표시되는 사진입니다.",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                )

                Icon(
                    painterResource(savedModel),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 2.dp),
                    tint = Color.Unspecified
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 17.dp, end = 8.dp)
                .weight(1f),
        ) {
            Text(
                text = restName,
                style = KusTheme.typography.type16b.copy(
                    color = KusTheme.colors.c_000000
                )
            )

            Row(
                modifier = Modifier.padding(top = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(Res.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = KusTheme.colors.c_AAAAAA
                )

                Text(
                    text = locationText,
                    style = KusTheme.typography.type12m.copy(
                        color = KusTheme.colors.c_AAAAAA
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        val tierIconRes = when (tier) {
            1 -> Res.drawable.ic_tier_1
            2 -> Res.drawable.ic_tier_2
            3 -> Res.drawable.ic_tier_3
            4 -> Res.drawable.ic_tier_4
            else -> Res.drawable.ic_rank_none
        }

        Box(
            modifier = Modifier
                .align(Alignment.Bottom),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(tierIconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}
