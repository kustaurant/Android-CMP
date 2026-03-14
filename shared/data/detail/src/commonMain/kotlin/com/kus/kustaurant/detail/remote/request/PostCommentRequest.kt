package com.kus.kustaurant.detail.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class PostCommentRequest(
    val body: String,
)
