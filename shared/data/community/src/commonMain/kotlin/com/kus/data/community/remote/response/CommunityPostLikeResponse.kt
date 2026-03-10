package com.kus.data.community.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostLikeResponse(
    val reactionType : String?,
    val likeCount : Int,
    val dislikeCount : Int,
    val netLikes : Int,
)