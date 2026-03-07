package com.kus.feature.detail.ui

import UiError
import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.detail.model.ReviewSort
import com.kus.feature.detail.state.DetailUiState
import com.kus.shared.domain.detail.usecase.DeleteRestaurantFavoriteUseCase
import com.kus.shared.domain.detail.usecase.GetRestaurantDetailUseCase
import com.kus.shared.domain.detail.usecase.GetRestaurantReviewsUseCase
import com.kus.shared.domain.detail.usecase.PostCommentUseCase
import com.kus.shared.domain.detail.usecase.PutCommentReactionUseCase
import com.kus.shared.domain.detail.usecase.PutEvaluationReactionUseCase
import com.kus.shared.domain.detail.usecase.PutRestaurantFavoriteUseCase
import com.kus.shared.domain.model.detail.RestaurantReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
    private val getRestaurantReviewsUseCase: GetRestaurantReviewsUseCase,
    private val putEvaluationReactionUseCase: PutEvaluationReactionUseCase,
    private val putCommentReactionUseCase: PutCommentReactionUseCase,
    private val putRestaurantFavoriteUseCase: PutRestaurantFavoriteUseCase,
    private val deleteRestaurantFavoriteUseCase: DeleteRestaurantFavoriteUseCase,
    private val postCommentUseCase: PostCommentUseCase,
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

    fun onFavoriteClick() = viewModelScope.launch {
        val currentState = _uiState.value.restaurant
        if (currentState !is UiState.Success) return@launch

        val current = currentState.data
        runCatching {
            if (current.isFavorite) {
                deleteRestaurantFavoriteUseCase(current.restaurantId)
            } else {
                putRestaurantFavoriteUseCase(current.restaurantId)
            }
        }.onSuccess { result ->
            _uiState.update { state ->
                val restaurantState = state.restaurant
                if (restaurantState !is UiState.Success) return@update state

                state.copy(
                    restaurant = UiState.Success(
                        restaurantState.data.copy(
                            isFavorite = result.isFavorite,
                            favoriteCount = result.favoriteCount
                        )
                    )
                )
            }
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
            putEvaluationReactionUseCase(evalId, newReaction)
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
            putCommentReactionUseCase(commentId, newReaction)
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

    fun postComment(evalId: Int, body: String) = viewModelScope.launch {
        runCatching {
            postCommentUseCase(currentRestaurantId, evalId, body)
        }.onSuccess { newComment ->
            updateReviewList { reviews ->
                reviews.map { review ->
                    if (review.evalId == evalId) {
                        review.copy(
                            evalCommentList = review.evalCommentList + newComment
                        )
                    } else review
                }
            }
        }
    }
}
