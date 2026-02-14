package com.kus.data.community.remote.response

data class CommunityCommentReactionResponse(
    val likeCount : Int,
    val dislikeCount : Int,
    val reactionType : String?,
)