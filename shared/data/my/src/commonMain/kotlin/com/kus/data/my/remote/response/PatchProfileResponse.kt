package com.kus.data.my.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class PatchProfileResponse(
    val nickname: String,
    val email: String,
    val phoneNumber: String?,
)
