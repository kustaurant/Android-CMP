package com.kus.feature.tier.ui.list

import UiState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.component.KusRestThumbnail
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.tier.ui.TierPhase
import com.kus.feature.tier.ui.TierViewModel
import com.kus.feature.tier.ui.popup.TierInfoPopup
import com.kus.shared.domain.model.tier.TierRestaurant
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kustaurant.shared.core.designsystem.generated.resources.img_no_result
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
fun TierListScreen(
    modifier: Modifier = Modifier,
    viewModel: TierViewModel,
    listState : LazyListState,
    onRestaurantClick: (TierRestaurant) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showTierInfo by rememberSaveable { mutableStateOf(false) }
    val isAITier = uiState.isAITier

    if (showTierInfo) {
        TierInfoPopup(onDismiss = { showTierInfo = false })
    }

    LaunchedEffect(Unit) {
        val pos = uiState.tierListLastPosition
        if (pos > 0) listState.scrollToItem(pos)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index -> viewModel.setTierListLastPosition(index) }
    }

    LaunchedEffect(uiState.scrollToTopTrigger) {
        listState.scrollToItem(0)
    }

    InfiniteScrollEffect(
        listState = listState,
        onLoadMore = { viewModel.fetchNextRestaurants() }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TierAiToggleRow(
            isAiTier = uiState.isAITier,
            onAiToggleClick = viewModel::toggleAiTier,
            onTierGuideClick = { showTierInfo = true }
        )

        when (val state = uiState.listState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    KusLoadingAnimation()
                }
            }

            is UiState.Failure -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KamelImage(
                        resource = asyncPainterResource("ic_kus_disable.png"),
                        contentDescription = "disabled image"
                    )

                    Spacer(Modifier.height(26.dp))

                    Text(
                        text = "알 수 없는 오류가 발생했어요.\n 잠시후 다시 시도해주세요.",
                        style = KusTheme.typography.type17sb,
                        color = KusTheme.colors.c_AAAAAA
                    )
                }
            }

            is UiState.Success -> {
                val list = state.data
                if(list.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(CoreRes.drawable.img_no_result),
                            contentDescription = null,
                        )
 
                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "카테고리에 해당하는 음식점이 없어요.",
                            style = KusTheme.typography.type17sb,
                            color = KusTheme.colors.c_AAAAAA
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().background(KusTheme.colors.c_F3F3F3),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(8.dp))}

                        items(
                            items = list,
                            key = { it.restaurantId }
                        ) { restaurant ->
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        ambientColor = Color.Transparent,
                                    )
                            ) {
                                val displayTier = if (isAITier || !restaurant.isTempTier) {
                                    restaurant.mainTier
                                } else {
                                    -1
                                }

                                KusRestThumbnail(
                                    modifier = Modifier.fillMaxWidth(),
                                    tier = displayTier,
                                    restName = restaurant.restaurantName,
                                    restThumbnail = restaurant.restaurantImgUrl,
                                    restAlliance = restaurant.partnershipInfo,
                                    categories = arrayListOf(restaurant.restaurantCuisine),
                                    location = restaurant.restaurantPosition,
                                    isSaved = restaurant.isFavorite,
                                    isEvaluated = restaurant.isEvaluated,
                                    isTempTier = restaurant.isTempTier,
                                    onClick = { onRestaurantClick(restaurant) }
                                )
                            }
                        }

                        item {
                            if (uiState.pageState.phase == TierPhase.Paging) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    KusLoadingAnimation()
                                }
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}
