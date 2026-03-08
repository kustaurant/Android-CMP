package com.kus.feature.my.ui.community

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.ui.state.MyCommentUiState
import com.kus.shared.domain.model.my.MyCommentItem
import com.kus.shared.domain.my.usecase.GetMyCommentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyCommentViewModel(
    private val getMyCommentsUseCase: GetMyCommentsUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(MyCommentUiState())
    val uiState = _uiState.asStateFlow()

    val dummyMyComments = listOf(
        MyCommentItem(
            postId = 201,
            postCategory = "맛집추천",
            postTitle = "강남 숨은 라멘 맛집 발견!",
            body = "저도 여기 가봤는데 국물이 진짜 진해요 👍",
            likeCount = 6,
            timeAgo = "1시간 전"
        ),
        MyCommentItem(
            postId = 202,
            postCategory = "자유게시판",
            postTitle = "요즘 날씨에 가기 좋은 카페 추천",
            body = "사진 보니까 분위기 좋아 보이네요. 주말에 가봐야겠어요!",
            likeCount = 3,
            timeAgo = "어제"
        ),
        MyCommentItem(
            postId = 203,
            postCategory = "질문",
            postTitle = "홍대 근처 가성비 술집 있을까요?",
            body = "홍대입구 쪽에 '청춘포차' 괜찮았어요!",
            likeCount = 1,
            timeAgo = "3일 전"
        )
    )

    fun getMyComments() = viewModelScope.launch {
        runCatching { getMyCommentsUseCase() }
            .onSuccess { result ->
                _uiState.update { it.copy(comments = UiState.Success(dummyMyComments)) }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(comments = UiState.Failure(UiError.Network))
                }
            }
    }
}