package com.kus.kustaurant.detail.api

import com.kus.kustaurant.detail.remote.response.CommentReactionResponse
import com.kus.kustaurant.detail.remote.response.DetailResponse
import com.kus.kustaurant.detail.remote.response.EvaluationReactionResponse
import com.kus.kustaurant.detail.remote.response.ReviewResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put

class DetailApi(
    private val client: HttpClient,
) {
    suspend fun getRestaurantDetail(restaurantId: Long): DetailResponse {
        return client.get("/api/v2/restaurants/$restaurantId").body()
    }

    suspend fun getRestaurantReviews(
        restaurantId: Long,
        sort: String = "POPULARITY"
    ): List<ReviewResponse> {
        return client.get("/api/v2/restaurants/$restaurantId/comments") {
            parameter("sort", sort)
        }.body()
    }

    suspend fun putEvaluationReaction(
        evaluationId: Int,
        reaction: String?,
    ): EvaluationReactionResponse {
        return client.put("/api/v2/auth/restaurants/evaluations/$evaluationId/reaction") {
            reaction?.let { parameter("reaction", it) }
        }.body()
    }

    suspend fun putCommentReaction(
        evalCommentId: Int,
        reaction: String?,
    ): CommentReactionResponse {
        return client.put("/api/v2/auth/eval-comments/$evalCommentId") {
            reaction?.let { parameter("reaction", it) }
        }.body()
    }
}
