package com.kus.feature.tier.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.shared.domain.model.tier.TierRestaurant
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_check
import kustaurant.shared.core.designsystem.generated.resources.ic_rank_none
import kustaurant.shared.core.designsystem.generated.resources.ic_saved
import kustaurant.shared.core.designsystem.generated.resources.ic_temp_tier_1
import kustaurant.shared.core.designsystem.generated.resources.ic_temp_tier_2
import kustaurant.shared.core.designsystem.generated.resources.ic_temp_tier_3
import kustaurant.shared.core.designsystem.generated.resources.ic_temp_tier_4
import kustaurant.shared.core.designsystem.generated.resources.ic_tier_1
import kustaurant.shared.core.designsystem.generated.resources.ic_tier_2
import kustaurant.shared.core.designsystem.generated.resources.ic_tier_3
import kustaurant.shared.core.designsystem.generated.resources.ic_tier_4
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TierRestaurantBottomSheetCard(
    restaurant: TierRestaurant,
    onClick: () -> Unit,
) {
    val colors = KusTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .noRippleClickable(onClick = onClick),
        shape = RoundedCornerShape(
            topStart = 18.dp,
            topEnd = 18.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        colors = CardDefaults.cardColors(containerColor = colors.c_FFFFFF),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                KamelImage(
                    resource = asyncPainterResource(restaurant.restaurantImgUrl),
                    contentDescription = "음식점 사진입니다.",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (restaurant.isFavorite) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_saved),
                            contentDescription = "즐겨찾기 아이콘입니다.",
                            tint = Color.Unspecified
                        )
                    }

                }
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val tierIconRes = if (restaurant.isTempTier) {
                    when (restaurant.mainTier) {
                        1 -> Res.drawable.ic_temp_tier_1
                        2 -> Res.drawable.ic_temp_tier_2
                        3 -> Res.drawable.ic_temp_tier_3
                        4 -> Res.drawable.ic_temp_tier_4
                        else -> Res.drawable.ic_rank_none
                    }
                } else {
                    when (restaurant.mainTier) {
                        1 -> Res.drawable.ic_tier_1
                        2 -> Res.drawable.ic_tier_2
                        3 -> Res.drawable.ic_tier_3
                        4 -> Res.drawable.ic_tier_4
                        else -> Res.drawable.ic_rank_none
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f, fill = false),
                            text = restaurant.restaurantName,
                            style = KusTheme.typography.type18sb,
                            color = KusTheme.colors.c_000000,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        if (restaurant.isEvaluated) {
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = vectorResource(Res.drawable.ic_check),
                                contentDescription = "평가완료 아이콘입니다.",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(18.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = vectorResource(tierIconRes),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }

                Text(
                    text = restaurant.toMetaLine(),
                    style = KusTheme.typography.type14r,
                    color = KusTheme.colors.c_666666,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                val benefit =
                    restaurant.partnershipInfo?.trim()?.takeIf { it.isNotEmpty() } ?: "제휴 해당 사항 없음"

                Text(
                    text = benefit,
                    style = KusTheme.typography.type14r,
                    color = KusTheme.colors.c_AAAAAA,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

private fun TierRestaurant.toMetaLine(): String {
    val cuisine = restaurantCuisine.takeIf { it.isNotBlank() } ?: "기타"
    val pos = restaurantPosition.takeIf { it.isNotBlank() } ?: "위치 정보 없음"
    return "$cuisine | $pos"
}
