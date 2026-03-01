package com.kus.data.community.remote.response

import com.kus.data.community.remote.request.PostCategoryRequest
import com.kus.domain.community.model.CommunityPost
import kotlinx.serialization.Serializable

@Serializable
data class CommunityCreatePostResponse(
    val postId: Long,
    val category: PostCategoryRequest,
    val title: String,
    val body: String,
    val photoUrls: List<String>?,
)

fun CommunityCreatePostResponse.toDomain() : CommunityPost =
    CommunityPost(
        postId = postId,
        category = category.toDomain(),
        title = title,
        body = body,
        photoUrls = photoUrls,
        writerId = -1L,
        nickname = "",
        evalCount = 0L,
        createdAt = "",
        likeOnlyCount = 0L,
        dislikeOnlyCount = 0L,
        totalLikes = 0L,
        commentCount = 0L,
        scrapCount = 0L,
        visitCount = 0L,
        isScrapped = false,
        isPostMine = true,
        writerIconUrl = null,
        timeAgo = "",
        updatedAt = null,
        myReaction = null,
        comments = null,
    )