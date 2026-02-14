package com.kus.data.community.remote.response

data class CommunityPostCommentResponse(
    val commentId: Long,
    val parentCommentId: Long?,
    val body: String,
    val status: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val replies: List<CommunityPostCommentResponse>?,
    val timeAgo: String,
    val reactionType: String?,
    val isCommentMine: Boolean,

    val writeruserId: Long?,
    val writernickname: String?,
    val writerevalCount: Long?,
    val writericonUrl: String?
)