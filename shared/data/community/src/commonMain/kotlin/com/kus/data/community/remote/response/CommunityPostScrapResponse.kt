package com.kus.data.community.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostScrapResponse(
    val postScrapCount : Int,
    val isScrapped : Boolean,
)