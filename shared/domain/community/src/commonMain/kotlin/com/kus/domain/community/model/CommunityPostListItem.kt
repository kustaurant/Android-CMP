package com.kus.domain.community.model

data class CommunityPostListItem(
    val postId: Long,
    val category: String,
    val title: String,
    val body: String,
    val writerId: Long,
    val nickname: String,
    val evalCount: Long,
    val writerIconUrl: String?,
    val photoUrl: String?,
    val timeAgo: String,
    val totalLikes: Long,
    val commentCount: Long,
)