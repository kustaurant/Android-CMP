package com.kus.shared.domain.model.my

data class FavoriteResItem(
    val restaurantName: String,
    val restaurantId: Int,
    val restaurantImgURL: String,
    val mainTier: Int,
    val restaurantType: String,
    val restaurantPosition: String,
)
