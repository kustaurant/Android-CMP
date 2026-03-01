package com.kus.shared.domain.model.detail

data class RestaurantDetail(
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
    val situationList: List<String>,
    val partnershipInfo: String,
    val evaluationCount: Int,
    val restaurantScore: Double?,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val favoriteCount: Int,
    val restaurantMenuList: List<RestaurantMenu>,
)

data class RestaurantMenu(
    val menuId: Long,
    val restaurantId: Long,
    val menuName: String,
    val menuPrice: String,
    val naverType: String,
    val menuImgUrl: String
)
