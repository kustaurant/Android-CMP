package com.kus.shared.domain.model.home

import com.kus.shared.domain.model.restaurant.RestaurantItem

data class HomeInfo(
    val topRestaurantsByRating: List<RestaurantItem>,
    val restaurantsForMe: List<RestaurantItem>,
    val photoUrls: List<String>,
)
