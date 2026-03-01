package com.kus.feature.community.model

import com.kus.domain.community.model.CommunityPost
import com.kus.domain.community.model.PostCategory
import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostModifyPayload(
    val postId: Long,
    val title: String,
    val body: String,
    val category: PostCategory,
    val totalLikes: Long,
    val commentCount: Long,
)

fun CommunityPost.toModifyPayload(): CommunityPostModifyPayload =
    CommunityPostModifyPayload(
        postId = postId,
        category = category,
        title = title,
        body = body,
        totalLikes = totalLikes,
        commentCount = commentCount,
    )