package com.kus.data.community.remote.response

import com.kus.data.community.remote.request.PostCategoryRequest

data class PostResponse(
    val postId: Long,
    val category: PostCategoryRequest,
    val title: String,
    val body: String,
    val photoUrls: List<String>
)