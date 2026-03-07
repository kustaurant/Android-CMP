package com.kus.kustaurant.detail.repositoryimpl

import com.kus.kustaurant.detail.api.DetailApi
import com.kus.kustaurant.detail.remote.mapper.toDomain
import com.kus.shared.domain.detail.repository.DetailRepository
import com.kus.shared.domain.model.detail.FavoriteResult
import com.kus.shared.domain.model.detail.ReactionResult
import com.kus.shared.domain.model.detail.RestaurantDetail
import com.kus.shared.domain.model.detail.RestaurantReview
import com.kus.shared.domain.model.detail.ReviewComment

class DetailRepositoryImpl(
    private val api: DetailApi,
) : DetailRepository {
    override suspend fun getRestaurantDetail(restaurantId: Long): RestaurantDetail =
        api.getRestaurantDetail(restaurantId).toDomain()

    override suspend fun getRestaurantReviews(restaurantId: Long, sort: String): List<RestaurantReview> =
        api.getRestaurantReviews(restaurantId, sort).map { it.toDomain() }

    override suspend fun putEvaluationReaction(
        evaluationId: Int,
        reaction: String?,
    ): ReactionResult =
        api.putEvaluationReaction(evaluationId, reaction).toDomain()

    override suspend fun putCommentReaction(
        evalCommentId: Int,
        reaction: String?,
    ): ReactionResult =
        api.putCommentReaction(evalCommentId, reaction).toDomain()

    override suspend fun putRestaurantFavorite(restaurantId: Long): FavoriteResult =
        api.putRestaurantFavorite(restaurantId).toDomain()

    override suspend fun deleteRestaurantFavorite(restaurantId: Long): FavoriteResult =
        api.deleteRestaurantFavorite(restaurantId).toDomain()

    override suspend fun postComment(restaurantId: Long, evalId: Int, body: String): ReviewComment =
        api.postComment(restaurantId, evalId, body).toDomain()

    override suspend fun deleteComment(restaurantId: Long, evalCommentId: Int) =
        api.deleteComment(restaurantId, evalCommentId)
}
