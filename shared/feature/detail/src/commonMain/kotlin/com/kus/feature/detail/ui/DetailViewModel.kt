package com.kus.feature.detail.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.detail.model.ReviewSort
import com.kus.feature.detail.state.DetailUiState
import com.kus.shared.domain.detail.usecase.GetRestaurantDetailUseCase
import com.kus.shared.domain.detail.usecase.GetRestaurantReviewsUseCase
import com.kus.shared.domain.detail.usecase.PutCommentReactionUseCase
import com.kus.shared.domain.detail.usecase.PutEvaluationReactionUseCase
import com.kus.shared.domain.model.detail.RestaurantReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
    private val getRestaurantReviewsUseCase: GetRestaurantReviewsUseCase,
    private val reactToEvaluationUseCase: PutCommentReactionUseCase,
    private val reactToCommentUseCase: PutEvaluationReactionUseCase,
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
        onReviewReactionClick(evalId, targetReaction = "LIKE")
    }

    fun onReviewDislikeClick(evalId: Int) {
        onReviewReactionClick(evalId, targetReaction = "DISLIKE")
    }

    fun onCommentLikeClick(evalId: Int, commentId: Int) {
        onCommentReactionClick(evalId, commentId, targetReaction = "LIKE")
    }

    fun onCommentDislikeClick(evalId: Int, commentId: Int) {
        onCommentReactionClick(evalId, commentId, targetReaction = "DISLIKE")
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

    private fun onReviewReactionClick(evalId: Int, targetReaction: String) = viewModelScope.launch {
        val currentReviews = _uiState.value.reviews
        if (currentReviews !is UiState.Success) return@launch

        val review = currentReviews.data.find { it.evalId == evalId } ?: return@launch
        val newReaction = setChangeReaction(
            currentReaction = review.reactionType,
            targetReaction = targetReaction
        )

        runCatching {
            reactToEvaluationUseCase(evalId, newReaction)
        }.onSuccess { result ->
            updateReviewList { reviews ->
                reviews.map {
                    if (it.evalId == evalId) {
                        it.copy(
                            reactionType = result.reaction,
                            evalLikeCount = result.likeCount,
                            evalDislikeCount = result.dislikeCount
                        )
                    } else it
                }
            }
        }
    }

    private fun onCommentReactionClick(
        evalId: Int,
        commentId: Int,
        targetReaction: String
    ) = viewModelScope.launch {
        val currentReviews = _uiState.value.reviews
        if (currentReviews !is UiState.Success) return@launch

        val review = currentReviews.data.find { it.evalId == evalId } ?: return@launch
        val comment = review.evalCommentList.find { it.commentId == commentId } ?: return@launch
        val newReaction = setChangeReaction(
            currentReaction = comment.reactionType,
            targetReaction = targetReaction
        )

        runCatching {
            reactToCommentUseCase(commentId, newReaction)
        }.onSuccess { result ->
            updateReviewList { reviews ->
                reviews.map {
                    if (it.evalId == evalId) {
                        it.copy(
                            evalCommentList = it.evalCommentList.map { currentComment ->
                                if (currentComment.commentId == commentId) {
                                    currentComment.copy(
                                        reactionType = result.reaction,
                                        commentLikeCount = result.likeCount,
                                        commentDislikeCount = result.dislikeCount
                                    )
                                } else currentComment
                            }
                        )
                    } else it
                }
            }
        }
    }

    private fun setChangeReaction(currentReaction: String, targetReaction: String): String? =
        if (currentReaction.equals(targetReaction, ignoreCase = true)) null else targetReaction
}
