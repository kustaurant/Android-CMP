package com.kus.feature.my.ui.restaurant

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.component.EmptyPage
import com.kus.feature.my.component.MyPageTopBar
import com.kus.feature.my.component.MyReviewItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CheckedRestaurantScreen(
    onBackClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    viewModel: CheckedResViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getEvaluatedRes()
    }

    when (uiState.value.restaurants) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                KusLoadingAnimation()
            }
        }

        is UiState.Success<*> -> {
            val restaurants = (uiState.value.restaurants as UiState.Success).data
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
                        .background(KusTheme.colors.c_FFFFFF)
                        .navigationBarsPadding(),
                ) {
                    stickyHeader {
                        MyPageTopBar(
                            title = "내가 남긴 평가",
                            onBackClick = onBackClick,
                        )
                    }

                    item {
                        Spacer(Modifier.height(10.dp))
                    }

                    itemsIndexed(restaurants) { index, item ->
                        Box(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                        ) {
                            MyReviewItem(
                                restaurantName = item.restaurantName,
                                restaurantImgURL = item.restaurantImgURL,
                                evaluationScore = item.evaluationScore.toFloat(),
                                evaluationBody = item.evaluationBody,
                                evaluationItemScores = item.evaluationItemScores,
                                onItemClick = { onItemClick(item.restaurantId.toLong()) },
                            )
                        }

                        HorizontalDivider(thickness = 1.dp, color = KusTheme.colors.c_EAEAEA)
                    }

                    item {
                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }

        is UiState.Failure -> { /* 서버 연결 실패 시 화면 */
        }

        is UiState.Idle -> {}
    }
}
