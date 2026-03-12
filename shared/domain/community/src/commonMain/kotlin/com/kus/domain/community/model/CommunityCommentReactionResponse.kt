package com.kus.domain.community.model

data class CommunityCommentReaction(
    val likeCount : Int,
    val dislikeCount : Int,
    val reactionType : String?,
)