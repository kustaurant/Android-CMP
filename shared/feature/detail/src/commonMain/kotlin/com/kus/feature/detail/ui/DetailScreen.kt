package com.kus.feature.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.feature.detail.component.DetailRestInfo
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
}

