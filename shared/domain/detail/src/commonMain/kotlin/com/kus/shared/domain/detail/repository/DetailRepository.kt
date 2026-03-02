package com.kus.shared.domain.detail.repository

import com.kus.shared.domain.model.detail.ReactionResult
import com.kus.shared.domain.model.detail.RestaurantDetail
import com.kus.shared.domain.model.detail.RestaurantReview

interface DetailRepository {
    suspend fun getRestaurantDetail(restaurantId: Long): RestaurantDetail
    suspend fun getRestaurantReviews(restaurantId: Long, sort: String): List<RestaurantReview>
    suspend fun putEvaluationReaction(evaluationId: Int, reaction: String?): ReactionResult
    suspend fun putCommentReaction(evalCommentId: Int, reaction: String?): ReactionResult
}
