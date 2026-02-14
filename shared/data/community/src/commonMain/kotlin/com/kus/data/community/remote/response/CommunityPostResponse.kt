package com.kus.data.community.remote.response

import com.kus.data.community.remote.request.PostCategoryRequest
import kotlinx.serialization.SerialName


data class CommunityPostResponse(
    val postId: Long,
    val category: PostCategoryRequest,
    val title: String,
    val body: String,
    val photoUrls: List<String>?,

    @SerialName("writeruserId")val writerId: Long,
    @SerialName("writernickname")val nickname: String,
    @SerialName("writerevalCount")val evalCount: Long,
    @SerialName("writericonUrl")val writerIconUrl: String?,

    val timeAgo: String?,
    val createdAt: String,
    val updatedAt: String?,

    val likeOnlyCount: Long,
    val dislikeOnlyCount: Long,
    val totalLikes: Long,
    val commentCount: Long,
    val scrapCount: Long,
    val visitCount: Long,

    val myReaction: String?,
    val isScrapped: Boolean,
    val isPostMine: Boolean,

    val comments: List<CommunityPostCommentResponse>?
)