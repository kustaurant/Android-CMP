package com.kus.feature.community.ui

import GetSessionAvailabilityUseCase
import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventEmitter
import com.kus.domain.community.model.ListSortType
import com.kus.domain.community.model.PostCategory
import com.kus.domain.community.model.RankingSortType
import com.kus.domain.community.model.toCategorySort
import com.kus.domain.community.usecase.GetCommunityPostListUseCase
import com.kus.domain.community.usecase.GetCommunityRankingListUseCase
import com.kus.feature.community.model.CommunityPostModifyPayload
import com.kus.logging.KusLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val getSessionAvailabilityUseCase: GetSessionAvailabilityUseCase,
    private val getCommunityPostListUseCase: GetCommunityPostListUseCase,
    private val getCommunityRankingListUseCase: GetCommunityRankingListUseCase,
    private val sessionEvents: SessionEventEmitter,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    init {
        ensureDataForCurrentTab(force = true)
    }

    fun clearToastMessage() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    suspend fun requireLogin(): Boolean {
        if (!getSessionAvailabilityUseCase()) {
            sessionEvents.emit(SessionEvent.LoginRequired)
            return false
        }
        return true
    }

    fun selectTab(tab: CommunityTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        ensureDataForCurrentTab()
    }

    fun changePostCategory(newCategory: PostCategory) {
        if (_uiState.value.postCategory == newCategory) return
        _uiState.update { it.copy(postCategory = newCategory) }
        fetchFirstPosts()
    }

    fun changeListSortOrder(newOrder: ListSortType) {
        if (_uiState.value.listSortType == newOrder) return
        _uiState.update { it.copy(listSortType = newOrder) }

        when (_uiState.value.selectedTab) {
            CommunityTab.POSTS -> fetchFirstPosts()
            CommunityTab.RANKING -> {
            }
        }
    }

    fun changeRankingSortType(newSort: RankingSortType) {
        if (_uiState.value.rankingSortType == newSort) return
        _uiState.update { it.copy(rankingSortType = newSort) }
        loadRanking()
    }

    fun loadNextPosts() {
        val s = _uiState.value.postPageState
        if (s.phase != CommunityPhase.Idle || s.isLastPage) return

        _uiState.update {
            it.copy(postPageState = s.copy(phase = CommunityPhase.Paging))
        }
        loadPosts(requestedPage = s.page + 1)
    }

    fun fetchFirstPosts() {
        _uiState.update {
            it.copy(
                postListState = UiState.Loading,
                postPageState = it.postPageState.copy(
                    phase = CommunityPhase.Refreshing,
                    page = 0,
                    isLastPage = false
                )
            )
        }
        loadPosts(requestedPage = 0)
    }

    private fun ensureDataForCurrentTab(force: Boolean = false) {
        when (_uiState.value.selectedTab) {
            CommunityTab.POSTS -> {
                val need = force || _uiState.value.postListState !is UiState.Success
                if (need) fetchFirstPosts()
            }

            CommunityTab.RANKING -> {
                val need = force || _uiState.value.rankingListState !is UiState.Success
                if (need) loadRanking()
            }
        }
    }

    private fun loadPosts(requestedPage: Int) {
        val snapshot = _uiState.value
        val page = snapshot.postPageState
        if (page.phase != CommunityPhase.Refreshing && page.phase != CommunityPhase.Paging) return

        val category = snapshot.postCategory
        val sort = snapshot.listSortType.toCategorySort()

        viewModelScope.launch {
            runCatching {
                getCommunityPostListUseCase(
                    category,
                    requestedPage,
                    sort
                )
            }.onSuccess { fetched ->
                val isLast = fetched.isEmpty()

                _uiState.update { cur ->
                    val currentList = (cur.postListState as? UiState.Success)?.data ?: emptyList()
                    val merged = if (requestedPage == 0) fetched else currentList + fetched
                    cur.copy(
                        postListState = UiState.Success(merged),
                        postPageState = cur.postPageState.copy(
                            phase = CommunityPhase.Idle,
                            page = requestedPage,
                            isLastPage = isLast
                        )
                    )
                }
            }.onFailure { e ->
                _uiState.update { cur ->
                    cur.copy(
                        postListState = UiState.Failure(
                            UiError.Message(
                                e.message ?: "게시글 리스트를 불러오는데 오류가 발생했습니다."
                            )
                        ),
                        postPageState = cur.postPageState.copy(phase = CommunityPhase.Idle),
                        toastMessage = "게시글 리스트를 불러오는데 오류가 발생했습니다."
                    )
                }
            }
        }
    }

    fun updatePost(payload: CommunityPostModifyPayload) {
        val currentState = uiState.value.postListState
        if (currentState !is UiState.Success) return

        val updatedList = currentState.data.map { item ->
            if (item.postId == payload.postId) {
                item.copy(
                    title = payload.title,
                    body = payload.body,
                    totalLikes = payload.totalLikes,
                    commentCount = payload.commentCount
                )
            } else item
        }

        _uiState.update { it.copy(postListState = UiState.Success(updatedList)) }
    }

    fun deletePost(postId: Long) {
        val currentState = uiState.value.postListState
        if (currentState !is UiState.Success) return

        val updatedList = currentState.data.filter { it.postId != postId }

        _uiState.update {
            it.copy(
                postListState = UiState.Success(updatedList),
                toastMessage = "게시글이 삭제되었어요."
            )
        }
    }

    private fun loadRanking() {
        _uiState.update { it.copy(rankingListState = UiState.Loading) }
        val snapshot = _uiState.value
        val sortType = snapshot.rankingSortType

        viewModelScope.launch {
            runCatching {
                getCommunityRankingListUseCase(sortType)
            }.onSuccess { list ->
                _uiState.update { cur ->
                    cur.copy(rankingListState = UiState.Success(list))
                }
            }.onFailure { e ->
                KusLog.e("CommunityViewModel", "loadRanking error", e)
                _uiState.update {
                    it.copy(
                        rankingListState = UiState.Failure(
                            UiError.Message(
                                e.message ?: "랭킹 리스트를 불러오는데 오류가 발생했습니다."
                            )
                        ),
                        toastMessage = "랭킹 리스트를 불러오는데 오류가 발생했습니다."
                    )
                }
            }
        }
    }
}