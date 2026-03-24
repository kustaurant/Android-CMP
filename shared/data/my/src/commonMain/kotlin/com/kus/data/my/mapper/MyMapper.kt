package com.kus.data.my.mapper

import com.kus.data.my.remote.response.MyInfoResponse
import com.kus.data.my.remote.response.PatchProfileResponse
import com.kus.shared.domain.model.my.MyInfo
import com.kus.shared.domain.model.my.ProfileInfo

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

fun PatchProfileResponse.toDomain(): ProfileInfo =
    ProfileInfo(
        nickname = nickname,
        email = email,
        phoneNumber = phoneNumber,
    )
