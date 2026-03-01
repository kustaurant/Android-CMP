package com.kus.feature.detail.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.detail.model.ReviewSort
import com.kus.feature.detail.state.DetailUiState
import com.kus.shared.domain.detail.usecase.GetRestaurantDetailUseCase
import com.kus.shared.domain.detail.usecase.GetRestaurantReviewsUseCase
import com.kus.shared.domain.model.detail.RestaurantReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
    private val getRestaurantReviewsUseCase: GetRestaurantReviewsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    private var currentRestaurantId: Long = 0L

    fun getRestaurantDetail(restaurantId: Long) = viewModelScope.launch {
        currentRestaurantId = restaurantId
        runCatching {
            getRestaurantDetailUseCase(restaurantId)
        }.onSuccess { detail ->
            _uiState.update { it.copy(restaurant = UiState.Success(detail)) }
        }.onFailure {
            _uiState.update { it.copy(restaurant = UiState.Failure(UiError.Network)) }
        }
    }

    fun getRestaurantReviewsIfNeeded() {
        val currentReviews = _uiState.value.reviews
        if (currentReviews is UiState.Success || currentReviews is UiState.Loading) return
        getRestaurantReviews(_uiState.value.reviewSort)
    }

    fun getRestaurantReviews(sort: ReviewSort) = viewModelScope.launch {
        _uiState.update { it.copy(reviews = UiState.Loading, reviewSort = sort) }
        val apiSort = when (sort) {
            ReviewSort.Popular -> "POPULARITY"
            ReviewSort.Latest -> "LATEST"
        }
        runCatching {
            getRestaurantReviewsUseCase(currentRestaurantId, apiSort)
        }.onSuccess { reviews ->
            _uiState.update { it.copy(reviews = UiState.Success(reviews)) }
        }.onFailure { e ->
            _uiState.update { it.copy(reviews = UiState.Failure(UiError.Network)) }
        }
    }

    fun onReviewLikeClick(evalId: Int) {
        updateReviewList { reviews ->
            reviews.map { review ->
                if (review.evalId == evalId) {
                    when (review.reactionType.uppercase()) {
                        "LIKE" -> review.copy(
                            reactionType = "NONE",
                            evalLikeCount = review.evalLikeCount - 1
                        )
                        "DISLIKE" -> review.copy(
                            reactionType = "LIKE",
                            evalLikeCount = review.evalLikeCount + 1,
                            evalDislikeCount = review.evalDislikeCount - 1
                        )
                        else -> review.copy(
                            reactionType = "LIKE",
                            evalLikeCount = review.evalLikeCount + 1
                        )
                    }
                } else review
            }
        }
    }

    fun onReviewDislikeClick(evalId: Int) {
        updateReviewList { reviews ->
            reviews.map { review ->
                if (review.evalId == evalId) {
                    when (review.reactionType.uppercase()) {
                        "DISLIKE" -> review.copy(
                            reactionType = "NONE",
                            evalDislikeCount = review.evalDislikeCount - 1
                        )
                        "LIKE" -> review.copy(
                            reactionType = "DISLIKE",
                            evalLikeCount = review.evalLikeCount - 1,
                            evalDislikeCount = review.evalDislikeCount + 1
                        )
                        else -> review.copy(
                            reactionType = "DISLIKE",
                            evalDislikeCount = review.evalDislikeCount + 1
                        )
                    }
                } else review
            }
        }
    }

    fun onCommentLikeClick(evalId: Int, commentId: Int) {
        updateReviewList { reviews ->
            reviews.map { review ->
                if (review.evalId == evalId) {
                    review.copy(
                        evalCommentList = review.evalCommentList.map { comment ->
                            if (comment.commentId == commentId) {
                                when (comment.reactionType.uppercase()) {
                                    "LIKE" -> comment.copy(
                                        reactionType = "NONE",
                                        commentLikeCount = comment.commentLikeCount - 1
                                    )
                                    "DISLIKE" -> comment.copy(
                                        reactionType = "LIKE",
                                        commentLikeCount = comment.commentLikeCount + 1,
                                        commentDislikeCount = comment.commentDislikeCount - 1
                                    )
                                    else -> comment.copy(
                                        reactionType = "LIKE",
                                        commentLikeCount = comment.commentLikeCount + 1
                                    )
                                }
                            } else comment
                        }
                    )
                } else review
            }
        }
    }

    fun onCommentDislikeClick(evalId: Int, commentId: Int) {
        updateReviewList { reviews ->
            reviews.map { review ->
                if (review.evalId == evalId) {
                    review.copy(
                        evalCommentList = review.evalCommentList.map { comment ->
                            if (comment.commentId == commentId) {
                                when (comment.reactionType.uppercase()) {
                                    "DISLIKE" -> comment.copy(
                                        reactionType = "NONE",
                                        commentDislikeCount = comment.commentDislikeCount - 1
                                    )
                                    "LIKE" -> comment.copy(
                                        reactionType = "DISLIKE",
                                        commentLikeCount = comment.commentLikeCount - 1,
                                        commentDislikeCount = comment.commentDislikeCount + 1
                                    )
                                    else -> comment.copy(
                                        reactionType = "DISLIKE",
                                        commentDislikeCount = comment.commentDislikeCount + 1
                                    )
                                }
                            } else comment
                        }
                    )
                } else review
            }
        }
    }

    fun onFavoriteClick() {
        val currentState = _uiState.value.restaurant
        if (currentState !is UiState.Success) return

        val current = currentState.data
        val newIsFavorite = !current.isFavorite
        val newFavoriteCount = if (newIsFavorite) current.favoriteCount + 1 else current.favoriteCount - 1
        _uiState.update {
            it.copy(
                restaurant = UiState.Success(
                    current.copy(isFavorite = newIsFavorite, favoriteCount = newFavoriteCount)
                )
            )
        }
    }

    private fun updateReviewList(transform: (List<RestaurantReview>) -> List<RestaurantReview>) {
        val currentReviews = _uiState.value.reviews
        if (currentReviews !is UiState.Success) return
        _uiState.update { it.copy(reviews = UiState.Success(transform(currentReviews.data))) }
    }
}
