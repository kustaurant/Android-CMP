package com.kus.data.community.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostListItemResponse(
    val postId: Long,
    val category: String,
    val title: String,
    val body: String,
    @SerialName("writeruserId") val writerId: Long,
    @SerialName("writernickname") val nickname: String,
    @SerialName("writerevalCount") val evalCount: Long,
    @SerialName("writericonUrl") val writerIconUrl: String?,
    val photoUrl: String?,
    val timeAgo: String,
    val totalLikes: Long,
    val commentCount: Long,
)
