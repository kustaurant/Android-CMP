package com.kus.feature.my.ui.community

import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.ui.state.MyScrapUiState
import com.kus.shared.domain.model.my.MyPostItem
import com.kus.shared.domain.my.usecase.GetMyScrapsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyScrapViewModel(
    private val getMyScrapsUseCase: GetMyScrapsUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(MyScrapUiState())
    val uiState = _uiState.asStateFlow()

    val dummyMyPosts = listOf(
        MyPostItem(
            postId = 1,
            postCategory = "맛집추천",
            postTitle = "성수동 브런치 맛집 다녀왔어요",
            postImgUrl = "https://picsum.photos/300/200?1",
            fullBody = "주말에 성수동 브런치 카페에 다녀왔는데 분위기도 좋고 음식도 정말 맛있었습니다. 특히 아보카도 토스트가 인상적이었어요. 다음에도 또 방문할 예정입니다.",
            likeCount = 12,
            commentCount = 4,
            timeAgo = "2시간 전",
            body = "성수동 브런치 카페 추천합니다!"
        ),
        MyPostItem(
            postId = 2,
            postCategory = "자유게시판",
            postTitle = "요즘 날씨에 가기 좋은 카페",
            postImgUrl = "https://picsum.photos/300/200?2",
            fullBody = "요즘 날씨가 좋아서 테라스 있는 카페를 찾다가 괜찮은 곳을 발견했습니다. 커피도 괜찮고 디저트도 다양해서 좋았어요.",
            likeCount = 7,
            commentCount = 2,
            timeAgo = "어제",
            body = "테라스 카페 좋아하시면 추천!"
        ),
        MyPostItem(
            postId = 3,
            postCategory = "질문",
            postTitle = "홍대 근처 가성비 술집 추천해주세요",
            postImgUrl = "https://picsum.photos/300/200?3",
            fullBody = "친구들이랑 홍대에서 가볍게 술 한잔하려고 하는데 가격 괜찮고 분위기 좋은 술집 있을까요? 추천 부탁드립니다.",
            likeCount = 3,
            commentCount = 6,
            timeAgo = "3일 전",
            body = "홍대 가성비 술집 찾고 있습니다."
        )
    )

    fun getScraps() = viewModelScope.launch {
        runCatching { getMyScrapsUseCase() }
            .onSuccess { _uiState.update { it.copy(articles = UiState.Success(dummyMyPosts)) } }
            .onFailure {  }
    }
}
