package com.kus.domain.community.model

data class PostModification (
    val postId: Long,
    val category: PostCategory,
    val title: String,
    val body: String,
    val photoUrls: List<String>
)