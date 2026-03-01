package com.kus.feature.detail.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.detail.model.DetailReview
import com.kus.feature.detail.model.DetailReviewComment
import com.kus.feature.detail.model.DetailReviewUiState
import com.kus.feature.detail.model.ReviewSort
import com.kus.feature.detail.state.DetailUiState
import com.kus.shared.domain.detail.usecase.GetRestaurantDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _reviewUiState = MutableStateFlow(DetailReviewUiState())
    val reviewUiState = _reviewUiState.asStateFlow()

    private val dummyReviewList = listOf(
        DetailReview(
            evalId = 1,
            evalScore = 4.5,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer1/80/80",
            writerNickname = "역병",
            timeAgo = "2분전",
            evalImgUrl = "https://picsum.photos/seed/review1/300/200",
            evalBody = "오 좀 맛있는데?",
            reactionType = "LIKE",
            evalLikeCount = 14,
            evalDislikeCount = 3,
            isEvaluationMine = true,
            evalCommentList = listOf(
                DetailReviewComment(
                    commentId = 1,
                    writerIconImgUrl = "https://picsum.photos/seed/commenter1/60/60",
                    writerNickname = "역병",
                    timeAgo = "2분전",
                    commentBody = "저두 이 의견에 동의합니당",
                    reactionType = "LIKE",
                    commentLikeCount = 14,
                    commentDislikeCount = 3,
                    isCommentMine = true,
                )
            )
        ),
        DetailReview(
            evalId = 2,
            evalScore = 4.0,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer2/80/80",
            writerNickname = "칼국수러버",
            timeAgo = "5분전",
            evalImgUrl = "https://picsum.photos/seed/review2/300/200",
            evalBody = "국물이 깔끔했어요.",
            reactionType = "LIKE",
            evalLikeCount = 9,
            evalDislikeCount = 1,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 3,
            evalScore = 3.5,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer3/80/80",
            writerNickname = "면덕후",
            timeAgo = "10분전",
            evalImgUrl = "https://picsum.photos/seed/review3/300/200",
            evalBody = "면이 쫄깃해서 좋았어요.",
            reactionType = "NONE",
            evalLikeCount = 4,
            evalDislikeCount = 0,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 4,
            evalScore = 5.0,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer4/80/80",
            writerNickname = "단골손님",
            timeAgo = "20분전",
            evalImgUrl = "https://picsum.photos/seed/review4/300/200",
            evalBody = "여긴 진짜 찐맛집!",
            reactionType = "LIKE",
            evalLikeCount = 32,
            evalDislikeCount = 2,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 5,
            evalScore = 4.2,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer5/80/80",
            writerNickname = "맛집탐방",
            timeAgo = "30분전",
            evalImgUrl = "https://picsum.photos/seed/review5/300/200",
            evalBody = "가격 대비 만족!",
            reactionType = "LIKE",
            evalLikeCount = 11,
            evalDislikeCount = 1,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 6,
            evalScore = 3.8,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer6/80/80",
            writerNickname = "배고파",
            timeAgo = "1시간전",
            evalImgUrl = "https://picsum.photos/seed/review6/300/200",
            evalBody = "양이 많아서 좋아요.",
            reactionType = "NONE",
            evalLikeCount = 6,
            evalDislikeCount = 1,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 7,
            evalScore = 4.7,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer7/80/80",
            writerNickname = "리뷰왕",
            timeAgo = "2시간전",
            evalImgUrl = "https://picsum.photos/seed/review7/300/200",
            evalBody = "재방문 의사 100%",
            reactionType = "LIKE",
            evalLikeCount = 21,
            evalDislikeCount = 0,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 8,
            evalScore = 3.2,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer8/80/80",
            writerNickname = "솔직후기",
            timeAgo = "3시간전",
            evalImgUrl = "https://picsum.photos/seed/review8/300/200",
            evalBody = "살짝 짰어요.",
            reactionType = "DISLIKE",
            evalLikeCount = 2,
            evalDislikeCount = 3,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 9,
            evalScore = 4.1,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer9/80/80",
            writerNickname = "맛알못",
            timeAgo = "4시간전",
            evalImgUrl = "https://picsum.photos/seed/review9/300/200",
            evalBody = "무난하게 맛있어요.",
            reactionType = "LIKE",
            evalLikeCount = 7,
            evalDislikeCount = 1,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 10,
            evalScore = 4.9,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer10/80/80",
            writerNickname = "최고최고",
            timeAgo = "1일전",
            evalImgUrl = "https://picsum.photos/seed/review10/300/200",
            evalBody = "여기만 오면 행복",
            reactionType = "LIKE",
            evalLikeCount = 40,
            evalDislikeCount = 0,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
    )

    fun getRestaurantDetail(restaurantId: Long) = viewModelScope.launch {
        runCatching {
            getRestaurantDetailUseCase(restaurantId)
        }.onSuccess { detail ->
            _uiState.update { it.copy(restaurant = UiState.Success(detail)) }
        }.onFailure {
            _uiState.update { it.copy(restaurant = UiState.Failure(UiError.Network)) }
        }
    }

    fun loadReviewsIfNeeded() {
        if (_reviewUiState.value.hasLoaded || _reviewUiState.value.isLoading) return
        loadReviews(_reviewUiState.value.sort)
    }

    fun loadReviews(sort: ReviewSort) {
        _reviewUiState.value = _reviewUiState.value.copy(isLoading = true, sort = sort)
        val list = when (sort) {
            ReviewSort.Popular -> dummyReviewList
            ReviewSort.Latest -> dummyReviewList.reversed()
        }
        _reviewUiState.value = DetailReviewUiState(
            reviewList = list,
            isLoading = false,
            hasLoaded = true,
            sort = sort,
        )
    }

    fun onReviewLikeClick(evalId: Int) {
        val currentList = _reviewUiState.value.reviewList
        val updatedList = currentList.map { review ->
            if (review.evalId == evalId) {
                when (review.reactionType.uppercase()) {
                    "LIKE" -> {
                        review.copy(
                            reactionType = "NONE",
                            evalLikeCount = review.evalLikeCount - 1
                        )
                    }
                    "DISLIKE" -> {
                        review.copy(
                            reactionType = "LIKE",
                            evalLikeCount = review.evalLikeCount + 1,
                            evalDislikeCount = review.evalDislikeCount - 1
                        )
                    }
                    else -> {
                        review.copy(
                            reactionType = "LIKE",
                            evalLikeCount = review.evalLikeCount + 1
                        )
                    }
                }
            } else {
                review
            }
        }
        _reviewUiState.value = _reviewUiState.value.copy(reviewList = updatedList)
    }

    fun onReviewDislikeClick(evalId: Int) {
        val currentList = _reviewUiState.value.reviewList
        val updatedList = currentList.map { review ->
            if (review.evalId == evalId) {
                when (review.reactionType.uppercase()) {
                    "DISLIKE" -> {
                        review.copy(
                            reactionType = "NONE",
                            evalDislikeCount = review.evalDislikeCount - 1
                        )
                    }
                    "LIKE" -> {
                        review.copy(
                            reactionType = "DISLIKE",
                            evalLikeCount = review.evalLikeCount - 1,
                            evalDislikeCount = review.evalDislikeCount + 1
                        )
                    }
                    else -> {
                        review.copy(
                            reactionType = "DISLIKE",
                            evalDislikeCount = review.evalDislikeCount + 1
                        )
                    }
                }
            } else {
                review
            }
        }
        _reviewUiState.value = _reviewUiState.value.copy(reviewList = updatedList)
    }

    fun onCommentLikeClick(evalId: Int, commentId: Int) {
        val currentList = _reviewUiState.value.reviewList
        val updatedList = currentList.map { review ->
            if (review.evalId == evalId) {
                val updatedComments = review.evalCommentList.map { comment ->
                    if (comment.commentId == commentId) {
                        when (comment.reactionType.uppercase()) {
                            "LIKE" -> {
                                comment.copy(
                                    reactionType = "NONE",
                                    commentLikeCount = comment.commentLikeCount - 1
                                )
                            }
                            "DISLIKE" -> {
                                comment.copy(
                                    reactionType = "LIKE",
                                    commentLikeCount = comment.commentLikeCount + 1,
                                    commentDislikeCount = comment.commentDislikeCount - 1
                                )
                            }
                            else -> {
                                comment.copy(
                                    reactionType = "LIKE",
                                    commentLikeCount = comment.commentLikeCount + 1
                                )
                            }
                        }
                    } else {
                        comment
                    }
                }
                review.copy(evalCommentList = updatedComments)
            } else {
                review
            }
        }
        _reviewUiState.value = _reviewUiState.value.copy(reviewList = updatedList)
    }

    fun onCommentDislikeClick(evalId: Int, commentId: Int) {
        val currentList = _reviewUiState.value.reviewList
        val updatedList = currentList.map { review ->
            if (review.evalId == evalId) {
                val updatedComments = review.evalCommentList.map { comment ->
                    if (comment.commentId == commentId) {
                        when (comment.reactionType.uppercase()) {
                            "DISLIKE" -> {
                                comment.copy(
                                    reactionType = "NONE",
                                    commentDislikeCount = comment.commentDislikeCount - 1
                                )
                            }
                            "LIKE" -> {
                                comment.copy(
                                    reactionType = "DISLIKE",
                                    commentLikeCount = comment.commentLikeCount - 1,
                                    commentDislikeCount = comment.commentDislikeCount + 1
                                )
                            }
                            else -> {
                                comment.copy(
                                    reactionType = "DISLIKE",
                                    commentDislikeCount = comment.commentDislikeCount + 1
                                )
                            }
                        }
                    } else {
                        comment
                    }
                }
                review.copy(evalCommentList = updatedComments)
            } else {
                review
            }
        }
        _reviewUiState.value = _reviewUiState.value.copy(reviewList = updatedList)
    }

    fun onFavoriteClick() {
        val currentState = _uiState.value.restaurant
        if (currentState !is UiState.Success) return

        val current = currentState.data
        val newIsFavorite = !current.isFavorite
        val newFavoriteCount = if (newIsFavorite) {
            current.favoriteCount + 1
        } else {
            current.favoriteCount - 1
        }
        _uiState.update {
            it.copy(
                restaurant = UiState.Success(
                    current.copy(
                        isFavorite = newIsFavorite,
                        favoriteCount = newFavoriteCount
                    )
                )
            )
        }
    }
}
