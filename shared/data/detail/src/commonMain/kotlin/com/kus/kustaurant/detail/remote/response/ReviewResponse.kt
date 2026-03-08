package com.kus.kustaurant.detail.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val evalId: Int,
    val evalScore: Double,
    val writerIconImgUrl: String? = null,
    val writerNickname: String,
    val timeAgo: String,
    val evalImgUrl: String? = null,
    val evalBody: String? = null,
    val reactionType: String? = null,
    val evalLikeCount: Int,
    val evalDislikeCount: Int,
    val isEvaluationMine: Boolean,
    val evalCommentList: List<ReviewCommentResponse>? = null,
)

@Serializable
data class ReviewCommentResponse(
    val commentId: Int,
    val writerIconImgUrl: String? = null,
    val writerNickname: String,
    val timeAgo: String,
    val commentBody: String? = null,
    val reactionType: String? = null,
    val commentLikeCount: Int,
    val commentDislikeCount: Int,
    val isCommentMine: Boolean,
)
