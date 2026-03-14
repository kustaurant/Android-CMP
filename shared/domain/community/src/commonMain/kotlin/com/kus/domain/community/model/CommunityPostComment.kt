package com.kus.domain.community.model

data class CommunityPostComment(
    val commentId: Long,
    val parentCommentId: Long? = null,
    val body: String,
    val status: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val repliesList: List<CommunityPostComment> = emptyList(),
    val timeAgo: String,
    val reactionType: String?,
    val isCommentMine: Boolean,
    val writerId: Long?,
    val nickname: String?,
    val evalCount: Long?,
    val writerIconUrl: String?,
)