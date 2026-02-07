package com.kus.feature.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.detail.component.DetailMenuItem
import com.kus.feature.detail.component.DetailRestInfo
import com.kus.feature.detail.component.DetailReviewItem
import com.kus.feature.detail.component.DetailTabSection
import kustaurant.shared.feature.detail.generated.resources.Res
import kustaurant.shared.feature.detail.generated.resources.img_rest_example
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailScreen() {
    val viewModel = remember { DetailViewModel() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reviewUiState by viewModel.reviewUiState.collectAsStateWithLifecycle()
    val restaurant = uiState.restaurant

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            val imageHeight = 269.dp
            val overlap = 100.dp
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(Res.drawable.img_rest_example),
                    contentDescription = "식당 이미지 사진",
                    modifier = Modifier.fillMaxWidth()
                        .height(imageHeight),
                    contentScale = ContentScale.Crop
                )

                DetailRestInfo(
                    situationList = restaurant.situationList,
                    mainTier = restaurant.mainTier,
                    restaurantCuisine = restaurant.restaurantCuisine,
                    restaurantCuisineImgUrl = restaurant.restaurantCuisineImgUrl,
                    restaurantPosition = restaurant.restaurantPosition,
                    restaurantName = restaurant.restaurantName,
                    restaurantAddress = restaurant.restaurantAddress,
                    naverMapUrl = restaurant.naverMapUrl,
                    partnershipInfo = restaurant.partnershipInfo,
                    modifier = Modifier
                        .padding(top = imageHeight - overlap)
                )
            }
        }

        item {
            DetailTabSection(
                reviewCount = restaurant.evaluationCount,
                menuContent = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        restaurant.restaurantMenuList.forEachIndexed { index, menu ->
                            val topPadding = if (index == 0) 6.dp else 0.dp
                            val bottomPadding =
                                if (index == restaurant.restaurantMenuList.lastIndex) 6.dp else 0.dp
                            DetailMenuItem(
                                menu = menu,
                                modifier = Modifier.padding(
                                    top = topPadding,
                                    bottom = bottomPadding
                                )
                            )
                            if (index != restaurant.restaurantMenuList.lastIndex) {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                        .height(1.dp)
                                        .background(color = KusTheme.colors.c_EAEAEA)
                                )
                            }
                        }
                    }
                },
                reviewContent = {
                    if (reviewUiState.reviewList.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ReviewSortRow(
                                selected = reviewUiState.sort,
                                onSelected = { sort ->
                                    viewModel.loadReviews(sort)
                                }
                            )

                            reviewUiState.reviewList.forEachIndexed { index, review ->
                                DetailReviewItem(
                                    review = review
                                )
                                if (index != reviewUiState.reviewList.lastIndex) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth()
                                            .height(1.dp)
                                            .background(color = KusTheme.colors.c_EAEAEA)
                                    )
                                }
                            }
                        }
                    }
                },
                onReviewTabSelected = {
                    viewModel.loadReviewsIfNeeded()
                }
            )
        }
    }
}

@Composable
private fun ReviewSortRow(
    selected: ReviewSort,
    onSelected: (ReviewSort) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SortText(
            text = "인기순",
            selected = selected == ReviewSort.Popular,
            onClick = { onSelected(ReviewSort.Popular) }
        )
        Text(
            text = " | ",
            style = KusTheme.typography.type12r.copy(
                color = KusTheme.colors.c_AAAAAA
            )
        )
        SortText(
            text = "최신순",
            selected = selected == ReviewSort.Latest,
            onClick = { onSelected(ReviewSort.Latest) }
        )
    }
}

@Composable
private fun SortText(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val color = if (selected) KusTheme.colors.c_43AB38 else KusTheme.colors.c_AAAAAA
    val textStyle = if (selected) KusTheme.typography.type14b else KusTheme.typography.type14r
    Text(
        text = text,
        modifier = Modifier.clickable(onClick = onClick),
        style = textStyle.copy(color = color)
    )
}
