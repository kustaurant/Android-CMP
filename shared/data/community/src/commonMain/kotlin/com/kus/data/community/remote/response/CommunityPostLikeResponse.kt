package com.kus.data.community.remote.response

data class CommunityPostLikeResponse(
    val reactionType : String?,
    val likeCount : Int,
    val dislikeCount : Int,
    val netLikes : Int,
)