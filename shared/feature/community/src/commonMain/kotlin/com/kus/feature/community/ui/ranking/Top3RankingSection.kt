package com.kus.feature.community.ui.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kus.domain.community.model.CommunityRanking
import kustaurant.shared.feature.community.generated.resources.Res
import kustaurant.shared.feature.community.generated.resources.bg_halo
import org.jetbrains.compose.resources.painterResource

@Composable
fun Top3RankingSection(
    top3: List<CommunityRanking>,
) {
    val first = top3.firstOrNull { it.rank == 1 }
    val second = top3.firstOrNull { it.rank == 2 }
    val third = top3.firstOrNull { it.rank == 3 }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.bg_halo),
            contentDescription = "평가 랭킹 탭 배경 이미지입니다.",
            modifier = Modifier.fillMaxSize(),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommunityRankingTopCard(
                rank = 2,
                data = second,
                sizeType = RankCardSize.Side,
                cardShape = RoundedCornerShape(
                    topStart = 14.dp,
                    bottomStart = 14.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .offset(y = 20.dp)
            )

            CommunityRankingTopCard(
                rank = 1,
                data = first,
                sizeType = RankCardSize.Center,
                cardShape = RoundedCornerShape(16.dp),
                isFirst = true,
                modifier = Modifier
                    .weight(1.3f)
                    .zIndex(1f)
                    .scale(1.05f)
            )

            CommunityRankingTopCard(
                rank = 3,
                data = third,
                sizeType = RankCardSize.Side,
                cardShape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 14.dp,
                    bottomEnd = 14.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .offset(y = 20.dp)
            )
        }
    }
}