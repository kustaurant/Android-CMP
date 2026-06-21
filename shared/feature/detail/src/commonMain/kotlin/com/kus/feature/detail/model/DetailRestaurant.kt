package com.kus.feature.detail.model

data class DetailRestaurant(
    val restaurantId: Int,
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
    val situationList: ArrayList<String>,
    val partnershipInfo: String,
    val evaluationCount: Int,
    val restaurantScore: Double,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val favoriteCount: Int,
    val restaurantMenuList: List<DetailRestaurantMenu>,
)

data class DetailRestaurantMenu(
    val menuId: Int,
    val restaurantId: Int,
    val menuName: String,
    val menuPrice: String,
    val menuImgUrl: String,
)
