package com.kus.data.my.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class MyInfoResponse(
    val nickname: String,
    val savedRestaurantCnt: Int,
    val evalCnt: Int,
    val postCnt: Int,
    val postCommentCnt: Int,
    val savedPostCnt: Int,
    val email: String,
    val phoneNumber: String?,
    val iconUrl: String,
)