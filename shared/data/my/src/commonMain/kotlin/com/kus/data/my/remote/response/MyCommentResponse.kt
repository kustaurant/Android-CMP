package com.kus.data.my.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class MyCommentResponse(
    val postId: Int,
    val postCategory: String,
    val postTitle: String,
    val body: String,
    val likeCount: Int,
    val timeAgo: String,
)
