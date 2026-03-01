package com.kus.feature.community.ui.ranking

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.domain.community.model.RankingSortType
import com.kus.feature.community.ui.CommunityTab
import com.kus.feature.community.ui.CommunityUiState

@Composable
fun RankingContent(
    uiState: CommunityUiState,
    onSortChange: (RankingSortType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sort = uiState.rankingSortType

    LaunchedEffect(uiState.selectedTab) {
        if (uiState.selectedTab == CommunityTab.RANKING) {
            val hasData = (uiState.rankingListState as? UiState.Success)?.data?.isNotEmpty() == true
            if (!hasData) onSortChange(sort)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        RankingHeader(
            selectedSort = sort,
            onSortChange = onSortChange,
        )

        Spacer(Modifier.height(10.dp))

        when (val state = uiState.rankingListState) {
            is UiState.Loading, UiState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = KusTheme.colors.c_43AB38)
                }
            }

            is UiState.Failure -> {
                Text(
                    text = "랭킹을 불러오지 못했어요.",
                    modifier = Modifier.padding(16.dp),
                    color = KusTheme.colors.c_666666,
                    style = KusTheme.typography.type14r
                )
            }

            is UiState.Success -> {
                val list = state.data.sortedBy { it.rank }
                val top3 = list.filter { it.rank in 1..3 }
                val rest = list.filter { it.rank >= 4 }

                Top3RankingSection(top3 = top3)

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                ) {
                    items(
                        items = rest,
                        key = { it.userId },
                    ) { item ->
                        RankingRow(item = item)
                    }
                }
            }
        }
    }
}