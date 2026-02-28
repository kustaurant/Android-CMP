package com.kus.data.community.remote.response

import kotlinx.serialization.SerialName
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
    @SerialName("writeruserId") val writerId: Long,
    @SerialName("writernickname") val nickname: String,
    @SerialName("writerevalCount") val evalCount: Long,
    @SerialName("writericonUrl") val writerIconUrl: String?,
)