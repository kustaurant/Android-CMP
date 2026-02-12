package com.kus.shared.domain.model.tier

data class TierRestaurant(
    val restaurantId: Long,
    val restaurantRanking: Int,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val isTempTier: Boolean,
    val longitude: Double,
    val latitude: Double,
    val partnershipInfo: String,
    val restaurantScore: Double,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
)