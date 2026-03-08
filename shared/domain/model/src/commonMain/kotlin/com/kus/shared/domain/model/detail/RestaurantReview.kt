package com.kus.shared.domain.model.detail

data class RestaurantReview(
    val evalId: Int,
    val evalScore: Double,
    val writerIconImgUrl: String,
    val writerNickname: String,
    val timeAgo: String,
    val evalImgUrl: String,
    val evalBody: String,
    val reactionType: String,
    val evalLikeCount: Int,
    val evalDislikeCount: Int,
    val isEvaluationMine: Boolean,
    val evalCommentList: List<ReviewComment>,
)

data class ReviewComment(
    val commentId: Int,
    val writerIconImgUrl: String,
    val writerNickname: String,
    val timeAgo: String,
    val commentBody: String,
    val reactionType: String,
    val commentLikeCount: Int,
    val commentDislikeCount: Int,
    val isCommentMine: Boolean,
)
