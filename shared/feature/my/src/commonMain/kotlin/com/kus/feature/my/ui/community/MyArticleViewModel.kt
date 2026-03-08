package com.kus.feature.my.ui.community

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.ui.state.MyArticleUiState
import com.kus.shared.domain.model.my.MyPostItem
import com.kus.shared.domain.my.usecase.GetMyPostsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyArticleViewModel(
    private val getMyPostsUseCase: GetMyPostsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyArticleUiState())
    val uiState = _uiState.asStateFlow()

    val dummyMyPosts = listOf(
        MyPostItem(
            postId = 101,
            postCategory = "맛집추천",
            postTitle = "강남에서 찾은 가성비 파스타 맛집",
            postImgUrl = "https://picsum.photos/300/200?post1",
            fullBody = "강남역 근처에 있는 작은 파스타집인데 가격도 저렴하고 양도 많습니다. 특히 크림 파스타가 정말 맛있었어요. 웨이팅이 조금 있지만 기다릴 가치가 있습니다.",
            likeCount = 23,
            commentCount = 5,
            timeAgo = "2시간 전",
            body = "강남 가성비 파스타 맛집 추천합니다!"
        ),
        MyPostItem(
            postId = 102,
            postCategory = "자유게시판",
            postTitle = "요즘 핫한 마라탕집 다녀왔어요",
            postImgUrl = "https://picsum.photos/300/200?post2",
            fullBody = "친구 추천으로 마라탕집을 다녀왔는데 국물이 진하고 재료 선택도 다양해서 좋았습니다. 맵기 조절도 가능해서 부담 없이 먹을 수 있었어요.",
            likeCount = 15,
            commentCount = 3,
            timeAgo = "어제",
            body = "마라탕 좋아하시는 분들 추천!"
        ),
        MyPostItem(
            postId = 103,
            postCategory = "질문",
            postTitle = "홍대 근처 분위기 좋은 술집 추천해주세요",
            postImgUrl = "https://picsum.photos/300/200?post3",
            fullBody = "홍대에서 친구들이랑 가볍게 한잔하려고 하는데 분위기 좋은 술집 있을까요? 시끄럽지 않고 이야기하기 좋은 곳이면 좋겠습니다.",
            likeCount = 8,
            commentCount = 7,
            timeAgo = "3일 전",
            body = "홍대 술집 추천 부탁드립니다!"
        )
    )

    fun getMyArticle() = viewModelScope.launch {
        runCatching { getMyPostsUseCase() }
            .onSuccess { result ->
                _uiState.update { it.copy(articles = UiState.Success(dummyMyPosts)) }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(articles = UiState.Failure(UiError.Network))
                }
            }
    }
}