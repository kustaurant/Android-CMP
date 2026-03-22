package com.kus.data.my.remote.response.request

import kotlinx.serialization.Serializable

@Serializable
data class PatchProfileInfoRequest(
    val nickname: String,
    val phoneNumber: String,
)
