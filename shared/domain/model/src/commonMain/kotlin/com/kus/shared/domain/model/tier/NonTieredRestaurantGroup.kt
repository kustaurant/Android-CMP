package com.kus.shared.domain.model.tier

data class NonTieredRestaurantGroup(
    val zoom: Int,
    val tierRestaurants: List<TierRestaurant>
)