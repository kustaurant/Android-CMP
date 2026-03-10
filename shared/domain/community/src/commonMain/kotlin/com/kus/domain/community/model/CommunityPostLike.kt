package com.kus.domain.community.model


data class CommunityPostLike(
    val reactionType : String?,
    val likeCount : Int,
    val dislikeCount : Int,
    val netLikes : Int,
)