package com.kus.data.login.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class NaverLoginRequest(
    val provider: String,
    val providerId: String,
    val token: String,
    val authCode: String? = null,
)