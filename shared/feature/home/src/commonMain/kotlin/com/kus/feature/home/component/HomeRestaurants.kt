package com.kus.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kus.shared.domain.model.restaurant.RestaurantItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_check
import kustaurant.shared.core.designsystem.generated.resources.ic_filled_star
import kustaurant.shared.core.designsystem.generated.resources.ic_saved
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun HomeRestaurants(
    title: String,
    subtitle: String,
    restaurants: List<RestaurantItem>,
    onRestaurantClick: (Long) -> Unit,
) {
    Column(
        modifier = Modifier,
    ) {
        Text(
            text = title,
            style = KusTheme.typography.type20b,
            modifier = Modifier.padding(start = 20.dp),
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = KusTheme.typography.type13r,
            color = KusTheme.colors.c_AAAAAA,
            modifier = Modifier.padding(start = 20.dp),
        )

        Spacer(Modifier.height(14.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
        ) {
            items(restaurants) { restaurant ->
                RestaurantItem(
                    restaurantName = restaurant.restaurantName,
                    restaurantCuisine = restaurant.restaurantCuisine,
                    restaurantPosition = restaurant.restaurantPosition,
                    restaurantImgUrl = restaurant.restaurantImgUrl,
                    mainTier = restaurant.mainTier,
                    partnershipInfo = restaurant.partnershipInfo,
                    restaurantScore = restaurant.restaurantScore,
                    isFavorite = restaurant.isFavorite,
                    isChecked = restaurant.isEvaluated,
                    onItemClick = { onRestaurantClick(restaurant.restaurantId) },
                )
            }
        }
    }
}

@Composable
private fun RestaurantItem(
    restaurantName: String,
    restaurantCuisine: String,
    restaurantPosition: String,
    restaurantImgUrl: String,
    mainTier: Int,
    partnershipInfo: String,
    restaurantScore: Double?,
    isFavorite: Boolean,
    isChecked: Boolean,
    onItemClick: () -> Unit,
) {
    val partnershipInfo = partnershipInfo.ifEmpty { "해당사항 없음" }

    Column(
        modifier = Modifier
            .width(190.dp)
            .noRippleClickable(onItemClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.TopStart,
        ) {
            KamelImage(
                resource = { asyncPainterResource(restaurantImgUrl) },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(190f / 128f),
                onFailure = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(KusTheme.colors.c_F5F5F5),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "이미지 로드 오류",
                            style = KusTheme.typography.type14r,
                        )
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier.background(
                        KusTheme.colors.c_AAAAAA,
                        RoundedCornerShape(50.dp)
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "$mainTier",
                        style = KusTheme.typography.type14r,
                    )
                }

                if (isFavorite) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_saved),
                        contentDescription = "저장 아이콘",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        ) {

            Text(
                text = "#$restaurantCuisine #$restaurantPosition",
                style = KusTheme.typography.type12r,
                color = KusTheme.colors.c_323232,
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_filled_star),
                    contentDescription = "별점 아이콘",
                    modifier = Modifier.size(14.dp),
                    tint = Color.Unspecified,
                )

                Text(
                    text = "${restaurantScore ?: 0.0}",
                    style = KusTheme.typography.type12r,
                    color = KusTheme.colors.c_323232,
                )
            }
        }

        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = restaurantName,
                style = KusTheme.typography.type15m,
                color = KusTheme.colors.c_323232,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            if (isChecked) {
                Spacer(Modifier.width(2.dp))

                Icon(
                    painter = painterResource(Res.drawable.ic_check),
                    contentDescription = "체크 아이콘",
                    modifier = Modifier.size(14.dp),
                    tint = Color.Unspecified,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "제휴",
                style = KusTheme.typography.type10r,
                color = KusTheme.colors.c_AAAAAA,
                modifier = Modifier.border(
                    1.dp,
                    KusTheme.colors.c_AAAAAA,
                    RoundedCornerShape(10.dp)
                ).padding(horizontal = 4.dp, vertical = 2.dp)
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = partnershipInfo,
                style = KusTheme.typography.type13r,
                color = KusTheme.colors.c_AAAAAA,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}
