package com.kus.kustaurant.detail.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class DetailResponse(
    val restaurantId: Long,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val isTempTier: Boolean,
    val restaurantCuisine: String,
    val restaurantCuisineImgUrl: String,
    val restaurantPosition: String,
    val restaurantName: String,
    val restaurantAddress: String,
    val isOpen: Boolean,
    val businessHours: String,
    val naverMapUrl: String,
    val situationList: List<String>? = null,
    val partnershipInfo: String? = null,
    val evaluationCount: Int,
    val restaurantScore: Double? = null,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val favoriteCount: Int,
    val restaurantMenuList: List<MenuResponse>? = null,
)

@Serializable
data class MenuResponse(
    val menuName: String,
    val menuPrice: String,
)
