package com.kus.data.community.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostCommentResponse(
    val commentId: Long,
    val parentCommentId: Long?,
    val body: String,
    val status: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val createdAt : String?,
    val updatedAt : String?,
    val timeAgo: String,
    val reactionType: String?,
    val isCommentMine: Boolean,
    val replyCount: Int = 0,
    val replies: List<CommunityPostCommentResponse>?,
     val writeruserId: Long?,
     val writernickname: String?,
     val writerevalCount: Long?,
     val writericonUrl: String?,
)