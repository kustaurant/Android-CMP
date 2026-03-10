package com.kus.feature.community.ui

import UiState
import com.kus.domain.community.model.ListSortType
import com.kus.domain.community.model.CommunityPostListItem
import com.kus.domain.community.model.CommunityRanking
import com.kus.domain.community.model.RankingSortType
import com.kus.domain.community.model.PostCategory

data class CommunityPageState(
    val phase: CommunityPhase = CommunityPhase.Idle,
    val page: Int = 0,
    val isLastPage: Boolean = false
)

data class CommunityUiState(
    val selectedTab: CommunityTab = CommunityTab.POSTS,

    val postCategory: PostCategory = PostCategory.ALL,
    val postListState: UiState<List<CommunityPostListItem>> = UiState.Loading,
    val postPageState: CommunityPageState = CommunityPageState(phase = CommunityPhase.Refreshing, page = 0),

    val listSortType: ListSortType = ListSortType.LATEST,
    val rankingSortType: RankingSortType = RankingSortType.SEASONAL,
    val rankingListState: UiState<List<CommunityRanking>> = UiState.Loading,

    val toastMessage: String? = null,
)