package com.kus.data.community.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CommunityCommentReactionResponse(
    val likeCount : Int,
    val dislikeCount : Int,
    val reactionType : String?,
)