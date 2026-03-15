package com.kus.feature.my.ui.restaurant

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.component.EmptyPage
import com.kus.feature.my.component.KusFavoriteResThumbnail
import com.kus.feature.my.component.MyPageTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun FavoriteRestaurantScreen(
    onBackClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    viewModel: FavoriteResViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFavorites()
    }

    when (uiState.value.restaurants) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success<*> -> {
            val restaurants = (uiState.value.restaurants as UiState.Success).data
            if (restaurants.isEmpty()) {
                EmptyPage(
                    title = "저장한 맛집",
                    comment = "저장한 맛집이 없습니다.",
                    onBackClick = onBackClick,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KusTheme.colors.c_F3F3F3),
                ) {
                    stickyHeader {
                        MyPageTopBar(
                            title = "저장한 맛집",
                            onBackClick = onBackClick,
                        )
                    }

                    item {
                        Spacer(Modifier.height(30.dp))
                    }

                    itemsIndexed(restaurants) { index, item ->
                        Box(
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            KusFavoriteResThumbnail(
                                tier = item.mainTier,
                                restName = item.restaurantName,
                                restThumbnail = item.restaurantImgURL,
                                location = item.restaurantPosition,
                                isSaved = true,
                                onClick = { onItemClick(item.restaurantId.toLong()) },
                            )
                        }
                    }
                }
            }
        }

        is UiState.Failure -> { /* 서버 연결 실패 시 화면 */ }
        is UiState.Idle -> {}
    }
}
