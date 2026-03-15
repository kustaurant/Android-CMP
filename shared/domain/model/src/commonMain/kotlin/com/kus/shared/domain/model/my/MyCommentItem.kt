package com.kus.shared.domain.model.my

data class MyCommentItem(
    val postId: Int,
    val postCategory: String,
    val postTitle: String,
    val body: String,
    val likeCount: Int,
    val timeAgo: String,
)
