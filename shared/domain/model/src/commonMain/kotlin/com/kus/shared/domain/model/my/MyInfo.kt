package com.kus.shared.domain.model.my

data class MyInfo(
    val nickname: String,
    val savedRestaurantCnt: Int,
    val evalCnt: Int,
    val postCnt: Int,
    val postCommentCnt: Int,
    val savedPostCnt: Int,
    val email: String,
    val phoneNumber: String,
    val iconUrl: String,
)
