package com.kus.domain.community.model

data class CommunityPost(
    val postId: Long,
    val category: PostCategory,
    val title: String,
    val body: String,
    val photoUrls: List<String>?,

    val writerId: Long,
    val nickname: String,
    val evalCount: Long,
    val writerIconUrl: String?,

    val timeAgo: String,
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

    val comments: List<CommunityPostComment>?
)