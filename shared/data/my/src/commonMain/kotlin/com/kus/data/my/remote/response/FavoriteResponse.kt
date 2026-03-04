package com.kus.data.my.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponse(
    val restaurantName: String,
    val restaurantId: Int,
    val restaurantImgURL: String,
    val mainTier: Int,
    val restaurantType: String,
    val restaurantPosition: String,
)
