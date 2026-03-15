package com.kus.domain.draw.model

data class DrawRestaurant(
    val restaurantId: Int,
    val restaurantRanking: Int?,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val latitude: Double,
    val longitude: Double,
    val partnershipInfo: String?,
    val restaurantScore: Double?,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val cuisineImgUrl : String,
)
