package com.kus.feature.tier.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.component.KusRestThumbnail
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.tier.ui.map.TierMapPlatform
import com.kus.feature.tier.ui.popup.TierInfoPopup
import com.kus.shared.domain.model.tier.TierRestaurant
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import kustaurant.shared.core.designsystem.generated.resources.ic_alarm_off
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import kustaurant.shared.core.designsystem.generated.resources.ic_search
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes
import kustaurant.shared.feature.tier.generated.resources.Res
import kustaurant.shared.feature.tier.generated.resources.ic_ai_filter_off
import kustaurant.shared.feature.tier.generated.resources.ic_ai_filter_on
import kustaurant.shared.feature.tier.generated.resources.ic_filter
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TierScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onNavigateRestaurantDetail: () -> Unit = {},
) {
    val tabs = remember { TierTab.entries }
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()
    val viewModel: TierViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mapPlatform: TierMapPlatform = koinInject()
    val sharedMapInstance = mapPlatform.rememberMapInstance()

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onTabSelected(tabs[pagerState.currentPage])
    }

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {

        KusTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            leftIcon = painterResource(CoreRes.drawable.ic_arrow_back),
            rightFirstIcon = painterResource(CoreRes.drawable.ic_search),
            rightSecondIcon = painterResource(CoreRes.drawable.ic_alarm_off),
            onLeftClicked = onBackClick,
            onRightFirstClicked = onSearchClick,
            onRightSecondClicked = onAlarmClick,
        ) {
            TierCenterTabs(
                selectedIndex = pagerState.currentPage,
                onSelect = { index -> scope.launch { pagerState.animateScrollToPage(index) } }
            )
        }

        Box(Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (tabs[page]) {
                    TierTab.LIST -> {
                        TierListScreen(
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 44.dp)
                        )
                    }

                    TierTab.MAP -> {
                        mapPlatform.TierMapScreen(
                            modifier = Modifier.fillMaxSize(),
                            state = uiState.mapUiState,
                            mapInstance = sharedMapInstance,
                            onMapTapped = { viewModel.onMapTapped() },
                            onRestaurantSelected = { id -> viewModel.onRestaurantMarkerClicked(id) },
                            onBottomSheetClick = { id -> },
                            onCameraIdle = { camera ->
                                viewModel.onCameraIdle(camera)
                            }
                        )
                    }
                }
            }

            TierSelectedCategoryRow(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                selectedCategories = uiState.selectedCategories.toList(),
                onFilterClick = onFilterClick,
                onChipClick = { viewModel.setShowBottomSheet(true) },
            )
        }
    }
}

@Composable
private fun TierCenterTabs(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .height(2.dp),
                color = KusTheme.colors.c_43AB38
            )
        },
        divider = {},
    ) {
        TierTab.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onSelect(index) },
                selectedContentColor = Color.Black,
                unselectedContentColor = KusTheme.colors.c_AAAAAA,
            ) {
                Text(
                    text = tab.title,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 18.dp),
                    style = KusTheme.typography.type16sb,
                )
            }
        }
    }
}

@Composable
fun TierSelectedCategoryRow(
    selectedCategories: List<String>,
    onFilterClick: () -> Unit,
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    fadeWidth: Dp = 16.dp,
) {
    val listState = rememberLazyListState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Image(
            modifier = Modifier
                .noRippleClickable(onClick = onFilterClick)
                .size(32.dp),
            painter = painterResource(Res.drawable.ic_filter),
            contentDescription = null,
        )

        FadingEdgeLazyRow(
            state = listState,
            fadeWidth = fadeWidth,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 0.dp),
        ) {
            items(selectedCategories, key = { it }) { label ->
                KusChip(
                    chipName = label,
                    isSelected = true,
                    onClick = { onChipClick(label) },
                    modifier = modifier.height(32.dp)
                )
            }
        }
    }
}

@Composable
private fun FadingEdgeLazyRow(
    state: LazyListState,
    modifier: Modifier = Modifier,
    fadeWidth: Dp = 16.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit,
) {
    val showLeftFade by remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 0 || state.firstVisibleItemScrollOffset > 0
        }
    }
    val showRightFade by remember {
        derivedStateOf {
            val info = state.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            val totalCount = info.totalItemsCount
            if (totalCount == 0) return@derivedStateOf false

            val isLastItemVisible = lastVisible.index == totalCount - 1
            if (!isLastItemVisible) return@derivedStateOf true

            val viewportEnd = info.viewportEndOffset
            val lastItemEnd = lastVisible.offset + lastVisible.size
            lastItemEnd > viewportEnd
        }
    }

    val fadePx = with(LocalDensity.current) { fadeWidth.toPx() }

    LazyRow(
        state = state,
        modifier = modifier
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                val w = size.width

                val left = if (showLeftFade) fadePx else 0f
                val right = if (showRightFade) fadePx else 0f

                if (left == 0f && right == 0f) return@drawWithContent

                val brush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0.0f to Color.Transparent,
                        (left / w).coerceIn(0f, 1f) to Color.Black,
                        ((w - right) / w).coerceIn(0f, 1f) to Color.Black,
                        1.0f to Color.Transparent
                    ),
                    startX = 0f,
                    endX = w
                )

                drawRect(
                    brush = brush,
                    topLeft = Offset.Zero,
                    size = size,
                    blendMode = BlendMode.DstIn
                )
            },
        horizontalArrangement = horizontalArrangement,
        contentPadding = contentPadding,
        content = content
    )
}


@Composable
private fun TierAiToggleRow(
    modifier: Modifier = Modifier,
    onTierGuideClick: () -> Unit = {},
) {
    var isAiOn by rememberSaveable { mutableStateOf(false) }

    val iconRes = if (isAiOn) {
        Res.drawable.ic_ai_filter_on
    } else {
        Res.drawable.ic_ai_filter_off
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = KusTheme.colors.c_FFFFFF)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { isAiOn = !isAiOn }
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = "AI 기반 티어 보기",
                modifier = Modifier.padding(start = 6.dp),
                style = KusTheme.typography.type12m.copy(color = KusTheme.colors.c_AAAAAA)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "티어란?",
            style = KusTheme.typography.type12m.copy(
                color = KusTheme.colors.c_666666,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable(onClick = onTierGuideClick)
        )
    }
}

@Composable
fun TierListScreen(
    modifier: Modifier = Modifier,
    viewModel: TierViewModel,
    onRestaurantClick: (TierRestaurant) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showTierInfo by rememberSaveable { mutableStateOf(false) }

    if (showTierInfo) {
        TierInfoPopup(onDismiss = { showTierInfo = false })
    }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        val pos = uiState.tierListLastPosition
        if (pos > 0) listState.scrollToItem(pos)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index -> viewModel.setTierListLastPosition(index) }
    }

    InfiniteScrollEffect(
        listState = listState,
        onLoadMore = { viewModel.fetchNextRestaurants() }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_F3F3F3)
    ) {
        TierAiToggleRow(
            onTierGuideClick = { showTierInfo = true }
        )

        when (val state = uiState.listState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
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
                        KamelImage(
                            resource = asyncPainterResource("ic_kus_disable.png"),
                            contentDescription = "disabled image"
                        )

                        Spacer(Modifier.height(26.dp))

                        Text(
                            text = "카테고리에 해당하는 음식점이 없어요.",
                            style = KusTheme.typography.type17sb,
                            color = KusTheme.colors.c_AAAAAA
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item{ Spacer(modifier = Modifier.height(6.dp)) }

                        items(
                            items = list,
                            key = { it.restaurantId }
                        ) { restaurant ->
                            KusRestThumbnail(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onRestaurantClick(restaurant) },
                                tier = restaurant.mainTier,
                                restName = restaurant.restaurantName,
                                restThumbnail = restaurant.restaurantImgUrl,
                                restAlliance = restaurant.partnershipInfo,
                                categories = arrayListOf(restaurant.restaurantCuisine),
                                location = restaurant.restaurantPosition,
                                isSaved = restaurant.isFavorite,
                                isEvaluated = restaurant.isEvaluated,
                                onClick = { onRestaurantClick(restaurant) }
                            )
                        }

                        item {
                            if (uiState.pageState.phase == TierPhase.Paging) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
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

@Composable
private fun InfiniteScrollEffect(
    listState: LazyListState,
    buffer: Int = 4,
    onLoadMore: () -> Unit,
) {
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val total = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            total to lastVisible
        }.collect { (total, lastVisible) ->
            if (total > 0 && lastVisible >= total - 1 - buffer) {
                onLoadMore()
            }
        }
    }
}


