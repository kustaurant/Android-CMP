package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class NonTieredRestaurantGroupResponse(
    val zoom: Int,
    val tierRestaurants: List<RestaurantResponse>
)