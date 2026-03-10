package com.kus.data.community.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostUploadImageResponse(
    val imgUrl: String
)