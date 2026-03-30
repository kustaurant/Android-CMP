package com.kus.feature.community.ui.ranking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.zIndex
import com.kus.designsystem.theme.KusTheme
import com.kus.domain.community.model.CommunityRanking
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.ic_crown
import kustaurant.shared.core.designsystem.generated.resources.ic_user_placeholder
import kustaurant.shared.feature.community.generated.resources.Res
import kustaurant.shared.feature.community.generated.resources.ic_medal_1
import kustaurant.shared.feature.community.generated.resources.ic_medal_2
import kustaurant.shared.feature.community.generated.resources.ic_medal_3
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
fun CommunityRankingTopCard(
    rank: Int,
    data: CommunityRanking?,
    sizeType: RankCardSize,
    cardShape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    isFirst: Boolean = false,
) {
    val isCenter = sizeType == RankCardSize.Center
    val height = if (isCenter) 188.dp else 140.dp
    val medalSize = if (isCenter) 56.dp else 40.dp
    val avatarSize = if (isCenter) 100.dp else 62.dp
    val topPaddingHeight = if (isCenter) 12.dp else 8.dp
    val infoTypo = if (isCenter) KusTheme.typography.type16m else KusTheme.typography.type12r
    val bgColor = if (isCenter) KusTheme.colors.c_FFFFFF else KusTheme.colors.c_EAEAEA

    val medalRes = when (rank) {
        1 -> Res.drawable.ic_medal_1
        2 -> Res.drawable.ic_medal_2
        else -> Res.drawable.ic_medal_3
    }

    Box(
        modifier = modifier
            .padding(top = medalSize / 2),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = cardShape,
            border = if (isFirst) BorderStroke(1.dp, KusTheme.colors.c_43AB38) else null,
            shadowElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier
                    .defaultMinSize(minHeight = height)
                    .background(bgColor)
                    .padding(horizontal = 10.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(topPaddingHeight))

                UserAvator(url = data?.iconUrl, modifier = Modifier.size(avatarSize))

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (rank == 1) {
                        Image(
                            painter = painterResource(CoreRes.drawable.ic_crown),
                            contentDescription = "1등 왕관",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                    }
                    Text(
                        text = data?.nickname ?: "-",
                        style = infoTypo,
                        color = KusTheme.colors.c_323232,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "${data?.evaluationCount ?: 0}개",
                    style = infoTypo,
                    color = KusTheme.colors.c_43AB38,
                )
            }
        }

        Image(
            painter = painterResource(medalRes),
            contentDescription = "${rank}등 메달",
            modifier = Modifier
                .size(medalSize)
                .align(Alignment.TopCenter)
                .offset(y = -(medalSize / 2))
                .zIndex(2f)
        )
    }
}

@Composable
fun UserAvator(
    url: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (!url.isNullOrBlank()) {
            KamelImage(
                resource = asyncPainterResource(url),
                contentDescription = "사용자의 프로필 이미지 입니다.",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                onFailure = {
                    Image(
                        painter = painterResource(CoreRes.drawable.ic_user_placeholder),
                        contentDescription = "기본 프로필 이미지 입니다."
                    )
                },
            )
        } else {
            Image(
                painter = painterResource(CoreRes.drawable.ic_user_placeholder),
                contentDescription = "기본 프로필 이미지 입니다.",
                modifier = Modifier.size(62.dp)
            )
        }
    }
}

@Composable
fun RankingRow(
    item: CommunityRanking,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.rank.toString(),
                style = KusTheme.typography.type16sb,
                color = KusTheme.colors.c_43AB38,
                modifier = Modifier.widthIn(34.dp),
            )

            Spacer(Modifier.width(2.dp))

            UserAvator(url = item.iconUrl)

            Spacer(Modifier.width(2.dp))

            Text(
                text = item.nickname,
                style = KusTheme.typography.type16m,
                color = KusTheme.colors.c_323232,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "${item.evaluationCount}개",
                style = KusTheme.typography.type16r,
                color = KusTheme.colors.c_666666,
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = KusTheme.colors.c_E0E0E0
        )
    }
}

