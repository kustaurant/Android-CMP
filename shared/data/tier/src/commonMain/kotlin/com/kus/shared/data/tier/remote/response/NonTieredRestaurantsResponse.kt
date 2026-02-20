package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class NonTieredRestaurantsResponse(
    val zoom: Int,
    val restaurants: List<RestaurantResponse> = emptyList()
)