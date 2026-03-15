package com.kus.feature.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.kus.feature.search.util.applyHighlight
import com.kus.shared.domain.model.search.HighlightsItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_check
import kustaurant.shared.core.designsystem.generated.resources.ic_kus_blank
import kustaurant.shared.core.designsystem.generated.resources.ic_location
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
import kustaurant.shared.core.designsystem.generated.resources.ic_unsaved
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchResultThumbnail(
    modifier: Modifier = Modifier,
    tier: Int? = null,
    restName: String,
    restThumbnail: String? = null,
    cuisine: String? = null,
    location: String? = null,
    isTempTier: Boolean = false,
    isSaved: Boolean,
    isEvaluated: Boolean,
    matchedMenus: List<String>,
    titleHighlights: List<HighlightsItem>,
    categoryHighlights: List<HighlightsItem>,
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
                .size(80.dp)
                .clip(RoundedCornerShape(11.dp))
        ) {
            if (restThumbnail.isNullOrBlank()) {
                Image(
                    painter = painterResource(Res.drawable.ic_kus_blank),
                    contentDescription = "음식점 사진이 없을 시, 표시되는 사진입니다.",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp)
                )
            } else {
                KamelImage(
                    resource = asyncPainterResource(restThumbnail),
                    contentDescription = "음식점 사진입니다.",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp),
                    onFailure = {
                        Image(
                            painter = painterResource(Res.drawable.ic_kus_blank),
                            contentDescription = "음식점 사진이 없을 시, 표시되는 사진입니다.",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                )
            }

            Icon(
                painterResource(savedModel),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp),
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 17.dp, end = 8.dp)
                .weight(1f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = restName.applyHighlight(
                        highlights = titleHighlights,
                        highlightColor = KusTheme.colors.c_43AB38,
                    ),
                    style = KusTheme.typography.type16b.copy(
                        color = KusTheme.colors.c_000000
                    )
                )

                if (isEvaluated) {
                    Icon(
                        painterResource(Res.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 6.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 5.dp),
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


            if (cuisine != null) {
                Text(
                    text = "#" + cuisine.applyHighlight(
                        highlights = categoryHighlights,
                        highlightColor = KusTheme.colors.c_43AB38,
                    ),
                    style = KusTheme.typography.type12r,
                    color = KusTheme.colors.c_AAAAAA,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            if (matchedMenus.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(matchedMenus) { index, menu ->
                        Text(
                            text = if (index != matchedMenus.lastIndex) "$menu," else menu,
                            style = KusTheme.typography.type12r,
                            color = KusTheme.colors.c_353535,
                        )
                    }
                }
            }
        }

        val tierIconRes = if (isTempTier) {
            when (tier) {
                1 -> Res.drawable.ic_temp_tier_1
                2 -> Res.drawable.ic_temp_tier_2
                3 -> Res.drawable.ic_temp_tier_3
                4 -> Res.drawable.ic_temp_tier_4
                else -> Res.drawable.ic_rank_none
            }
        } else {
            when (tier) {
                1 -> Res.drawable.ic_tier_1
                2 -> Res.drawable.ic_tier_2
                3 -> Res.drawable.ic_tier_3
                4 -> Res.drawable.ic_tier_4
                else -> Res.drawable.ic_rank_none
            }
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
                modifier = Modifier.size(24.dp),
            )
        }
    }
}