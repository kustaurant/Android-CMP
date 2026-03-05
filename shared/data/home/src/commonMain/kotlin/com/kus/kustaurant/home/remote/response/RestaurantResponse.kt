package com.kus.kustaurant.home.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantResponse(
    val restaurantId: Long,
    val restaurantRanking: Int? = null,
    val restaurantName: String,
    val restaurantCuisine: String,
    val restaurantPosition: String,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val latitude: Double,
    val longitude: Double,
    val partnershipInfo: String? = null,
    val restaurantScore: Double? = null,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val cuisineImgUrl: String = "",
    val tierImgUrl: String? = "",
    val isTempTier: Boolean,
)
