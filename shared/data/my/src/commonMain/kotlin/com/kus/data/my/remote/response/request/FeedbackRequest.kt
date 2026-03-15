package com.kus.data.my.remote.response.request

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackRequest(
    val comment: String,
)
