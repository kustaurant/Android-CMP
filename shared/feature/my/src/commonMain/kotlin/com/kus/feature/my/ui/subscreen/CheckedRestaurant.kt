package com.kus.feature.my.ui.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kus.designsystem.component.KusRestThumbnail
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.component.EmptyPage
import com.kus.feature.my.component.MyPageTopBar
import com.kus.shared.domain.model.restaurant.RestaurantItem

@Composable
internal fun CheckedRestaurantScreen(
    onBackClick: () -> Unit,
    onItemClick: (Long) -> Unit,
) {
    val restaurants = emptyList<RestaurantItem>()

    if (restaurants.isEmpty()) {
        EmptyPage(
            title = "내가 남긴 평가",
            comment = "평가 기록이 없습니다.",
            onBackClick = onBackClick
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(KusTheme.colors.c_FFFFFF),
        ) {
            stickyHeader {
                MyPageTopBar(
                    title = "내가 남긴 평가",
                    onBackClick = onBackClick,
                )
            }

            itemsIndexed(restaurants) { index, item ->
                KusRestThumbnail(
                    tier = item.mainTier,
                    restName = item.restaurantName,
                    restThumbnail = item.restaurantImgUrl,
                    restAlliance = item.partnershipInfo,
                    categories = arrayListOf(item.restaurantCuisine),
                    location = item.restaurantPosition,
                    isTempTier = item.isTempTier,
                    isSaved = item.isFavorite,
                    isEvaluated = item.isEvaluated,
                    onClick = { onItemClick(item.restaurantId) },
                )
            }
        }
    }
}
