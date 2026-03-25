package com.kus.feature.tier.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.component.KusFadingEdgeLazyRow
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.feature.tier.ui.list.TierListScreen
import com.kus.feature.tier.ui.map.TierMapPlatform
import kotlinx.coroutines.launch
import kustaurant.shared.feature.tier.generated.resources.Res
import kustaurant.shared.feature.tier.generated.resources.ic_filter
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TierScreen(
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {},
    onShowMessage: (String) -> Unit,
    onNavigateRestaurantDetail: (Long, Boolean) -> Unit = { _, _ -> },
) {
    val tabs = remember { TierTab.entries }
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()
    val viewModel: TierViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mapPlatform: TierMapPlatform = koinInject()
    val sharedMapInstance = mapPlatform.rememberMapInstance()
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            onShowMessage(it)
            viewModel.clearToast()
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onTabSelected(tabs[pagerState.currentPage])
    }

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {

        KusTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
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
                            listState = listState,
                            onRestaurantClick = { restaurant ->
                                onNavigateRestaurantDetail(
                                    restaurant.restaurantId,
                                    uiState.filterState.isAiTierViewEnabled
                                )
                            },
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

        KusFadingEdgeLazyRow(
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


