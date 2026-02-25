package com.kus.shared.domain.model.restaurant

data class RestaurantItem(
    val restaurantId: Long,
    val restaurantRanking: Int,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val latitude: Double,
    val longitude: Double,
    val partnershipInfo: String,
    val restaurantScore: Double,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val cuisineImgUrl: String,
    val tierImgUrl: String?,
    val isTempTier: Boolean,
)
