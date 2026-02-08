package com.kus.feature.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.detail.component.DetailHeaderImage
import com.kus.feature.detail.component.DetailRestInfo
import com.kus.feature.detail.component.DetailTabSection
import kustaurant.shared.core.designsystem.generated.resources.Res
import kustaurant.shared.core.designsystem.generated.resources.ic_left_arrow
import kustaurant.shared.core.designsystem.generated.resources.ic_saved
import kustaurant.shared.core.designsystem.generated.resources.ic_unsaved
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailScreen() {
    val viewModel = remember { DetailViewModel() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reviewUiState by viewModel.reviewUiState.collectAsStateWithLifecycle()
    val restaurant = uiState.restaurant
    var restInfoTopInWindow by remember { mutableStateOf(Float.POSITIVE_INFINITY) }
    val useWhiteTopBar = restInfoTopInWindow <= 0f
    val topBarBackground = if (useWhiteTopBar) KusTheme.colors.c_FFFFFF else Color.Transparent
    val topBarIconTint = if (useWhiteTopBar) KusTheme.colors.c_000000 else null
    val evaluateButtonText = if (restaurant.isEvaluated) "다시 평가하기" else "맛집 평가하기"
    val favoriteIcon = if (restaurant.isFavorite) Res.drawable.ic_saved else Res.drawable.ic_unsaved
    val favoriteContentDescription = if (restaurant.isFavorite) "저장됨" else "저장 안됨"
    val favoriteCountText = restaurant.favoriteCount.toString()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                val imageHeight = 269.dp
                val overlap = 100.dp
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DetailHeaderImage(
                        imageHeight = imageHeight
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
                            .onGloballyPositioned { coordinates ->
                                restInfoTopInWindow = coordinates.positionInWindow().y
                            }
                    )
                }
            }

            item {
                DetailTabSection(
                    reviewCount = restaurant.evaluationCount,
                    menuList = restaurant.restaurantMenuList,
                    reviewList = reviewUiState.reviewList,
                    selectedSort = reviewUiState.sort,
                    onSortSelected = { sort -> viewModel.loadReviews(sort) },
                    onReviewTabSelected = {
                        viewModel.loadReviewsIfNeeded()
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            KusTopBar(
                leftIcon = Res.drawable.ic_left_arrow,
                leftIconModifier = Modifier.noRippleClickable {}
                    .padding(all = 5.dp),
                iconTint = topBarIconTint,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(topBarBackground)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = KusTheme.colors.c_FFFFFF)
                .border(
                    width = 1.dp,
                    color = KusTheme.colors.c_E0E0E0
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KusButton(
                enabled = true,
                buttonName = evaluateButtonText,
                modifier = Modifier.weight(1f),
                roundedCornerShape = RoundedCornerShape(50.dp),
                onClick = {}
            )

            Column(
                modifier = Modifier.padding(start = 18.dp, end = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(favoriteIcon),
                    contentDescription = favoriteContentDescription,
                    modifier = Modifier
                )

                Text(
                    text = favoriteCountText,
                    style = KusTheme.typography.type14r.copy(
                        color = KusTheme.colors.c_AAAAAA
                    )
                )
            }
        }
    }
}
