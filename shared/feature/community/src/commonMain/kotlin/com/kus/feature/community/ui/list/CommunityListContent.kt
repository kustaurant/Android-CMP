package com.kus.feature.community.ui.list

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kus.designsystem.component.KusLoadingAnimation
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.domain.community.model.ListSortType
import com.kus.domain.community.model.PostCategory
import com.kus.feature.community.ui.CommunityBoards
import com.kus.feature.community.ui.CommunityPhase
import com.kus.feature.community.ui.CommunityUiState

@Composable
fun CommunityListContent(
    uiState: CommunityUiState,
    boardExpanded: Boolean,
    onBoardExpandedChange: (Boolean) -> Unit,
    onPostClick: (Long) -> Unit,
    onBoardSelect: (PostCategory) -> Unit,
    onSortChange: (ListSortType) -> Unit,
    onLoadNext: () -> Unit = {},
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pullState = rememberPullToRefreshState()

    val isInitialLoading =
        (uiState.postListState is UiState.Loading || uiState.postListState is UiState.Idle) &&
                uiState.postPageState.phase != CommunityPhase.Paging

    val isPullRefreshing = uiState.postPageState.phase == CommunityPhase.Refreshing

    val isRefreshingForIndicator = isPullRefreshing || isInitialLoading

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            CommunityListFilterBar(
                selectedBoard = uiState.postCategory,
                onBoardClick = { onBoardExpandedChange(true) },
                boardExpanded = boardExpanded,
                boards = CommunityBoards.list,
                onBoardSelect = { selected ->
                    onBoardExpandedChange(false)
                    onBoardSelect(selected)
                    onRefresh()
                },
                onBoardDismiss = { onBoardExpandedChange(false) },
                sortType = uiState.listSortType,
                onSortChange = { sort ->
                    onSortChange(sort)
                    onRefresh()
                }
            )

            PullToRefreshBox(
                state = pullState,
                isRefreshing = isPullRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
                indicator = {}
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .background(KusTheme.colors.c_FFFFFF),
                ) {
                    item {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = KusTheme.colors.c_E0E0E0,
                        )
                    }

                    when (val state = uiState.postListState) {
                        is UiState.Loading, UiState.Idle -> {
                            item { Spacer(Modifier.height(12.dp)) }
                        }

                        is UiState.Failure -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "게시글을 불러오지 못했어요.",
                                        style = KusTheme.typography.type14r,
                                        color = KusTheme.colors.c_666666
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        text = "다시 시도",
                                        color = KusTheme.colors.c_43AB38,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .noRippleClickable { onRefresh() }
                                    )
                                }
                            }
                        }

                        is UiState.Success -> {
                            val list = state.data

                            if (list.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 40.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "게시글의 첫 번째 주인공이 되어주세요.",
                                            style = KusTheme.typography.type14r,
                                            color = KusTheme.colors.c_666666
                                        )
                                    }
                                }
                            } else {
                                itemsIndexed(
                                    items = list,
                                    key = { _, item -> item.postId }
                                ) { index, item ->
                                    CommunityListRow(
                                        item = item,
                                        modifier = Modifier.noRippleClickable { onPostClick(item.postId) }
                                    )

                                    val shouldLoadNext = index >= list.lastIndex - 3
                                    LaunchedEffect(shouldLoadNext) {
                                        if (shouldLoadNext && uiState.postPageState.phase == CommunityPhase.Idle) {
                                            onLoadNext()
                                        }
                                    }
                                }

                                if (uiState.postPageState.phase == CommunityPhase.Paging) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 18.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            KusLoadingAnimation()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        PullToRefreshDefaults.Indicator(
            state = pullState,
            isRefreshing = isRefreshingForIndicator,
            containerColor = KusTheme.colors.c_FFFFFF,
            color = KusTheme.colors.c_43AB38,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f)
        )
    }
}
