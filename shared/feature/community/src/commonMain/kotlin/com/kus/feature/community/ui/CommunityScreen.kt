package com.kus.feature.community.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusTopBar
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.community.ui.floatingButton.WriteFab
import com.kus.feature.community.ui.list.CommunityListContent
import com.kus.feature.community.ui.ranking.RankingContent
import kotlinx.coroutines.launch
import kustaurant.shared.core.designsystem.generated.resources.ic_alarm_off
import kustaurant.shared.core.designsystem.generated.resources.ic_arrow_back
import kustaurant.shared.core.designsystem.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberFabVisibleOnScrollUp
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    onShowMessage: (String) -> Unit = {},
    onPostClick: (Long) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
) {
    val viewModel: CommunityViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var boardExpanded by remember { mutableStateOf(false) }
    val (showWriteFab, fabScrollConnection) = rememberFabVisibleOnScrollUp()

    val scope = rememberCoroutineScope()

    val selectedIndex = remember(uiState.selectedTab) {
        CommunityTab.entries.indexOf(uiState.selectedTab).coerceAtLeast(0)
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            onShowMessage(it)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            Column(
                modifier.background(KusTheme.colors.c_FFFFFF)
            ) {
                KusTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    leftIcon = painterResource(CoreRes.drawable.ic_arrow_back),
                    rightFirstIcon = painterResource(CoreRes.drawable.ic_search),
                    rightSecondIcon = painterResource(CoreRes.drawable.ic_alarm_off),
                    onLeftClicked = onBackClick,
                    onRightFirstClicked = onSearchClick,
                    onRightSecondClicked = onAlarmClick,
                ) {
                    Text(
                        text = "커뮤니티",
                        style = KusTheme.typography.type17sb,
                        color = KusTheme.colors.c_323232
                    )
                }

                CommunityTabs(
                    selectedIndex = selectedIndex,
                    onSelect = { index ->
                        viewModel.onTabSelected(CommunityTab.entries[index])
                    }
                )
            }
        },
        floatingActionButton = {
            val visible = (uiState.selectedTab == CommunityTab.POSTS) && showWriteFab

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(150)) +
                        scaleIn(
                            initialScale = 0.85f,
                            animationSpec = tween(180, easing = FastOutSlowInEasing)
                        ),
                exit = fadeOut(animationSpec = tween(120)) +
                        scaleOut(
                            targetScale = 0.85f,
                            animationSpec = tween(160, easing = FastOutSlowInEasing)
                        )
            ) {
                WriteFab(onClick = {
                    scope.launch {
                        if (viewModel.checkLoginAndEmit()) {
                            onWriteClick()
                        }
                    }
                })
            }
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            when (uiState.selectedTab) {
                CommunityTab.POSTS -> CommunityListContent(
                    uiState = uiState,
                    boardExpanded = boardExpanded,
                    onBoardExpandedChange = { boardExpanded = it },
                    onPostClick = onPostClick,
                    onBoardSelect = viewModel::onPostCategoryChanged,
                    onSortChange = viewModel::onOrderChanged,
                    onLoadNext = viewModel::fetchNextPosts,
                    onRefresh = viewModel::fetchFirstPosts,
                    modifier = Modifier.nestedScroll(fabScrollConnection)
                )

                CommunityTab.RANKING -> RankingContent(
                    uiState = uiState,
                    onSortChange = viewModel::onRankingSortTypeChanged,
                )
            }
        }
    }
}

@Composable
private fun CommunityTabs(
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
        divider = {
            HorizontalDivider(
                thickness = 2.dp,
                color = KusTheme.colors.c_E0E0E0,
            )
        },
    ) {
        CommunityTab.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onSelect(index) },
                selectedContentColor = Color.Black,
                unselectedContentColor = KusTheme.colors.c_323232,
            ) {
                Text(
                    text = tab.title,
                    modifier = Modifier.padding(vertical = 18.dp, horizontal = 18.dp),
                    style = KusTheme.typography.type14r,
                )
            }
        }
    }
}