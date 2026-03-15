package com.kus.data.my.mapper

import com.kus.data.my.remote.response.MyInfoResponse
import com.kus.shared.domain.model.my.MyInfo

fun MyInfoResponse.toDomain(): MyInfo =
    MyInfo(
        nickname = nickname,
        savedRestaurantCnt = savedRestaurantCnt,
        evalCnt = evalCnt,
        postCnt = postCnt,
        postCommentCnt = postCommentCnt,
        savedPostCnt = savedPostCnt,
        email = email,
        phoneNumber = phoneNumber,
        iconUrl = iconUrl,
    )
