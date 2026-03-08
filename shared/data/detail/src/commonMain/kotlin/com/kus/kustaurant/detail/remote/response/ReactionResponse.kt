package com.kus.kustaurant.detail.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationReactionResponse(
    val evaluationId: Int,
    val reaction: String? = null,
    val likeCount: Int,
    val dislikeCount: Int,
)

@Serializable
data class CommentReactionResponse(
    val evalCommentId: Int,
    val reaction: String? = null,
    val likeCount: Int,
    val dislikeCount: Int,
)
