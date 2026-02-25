package com.kus.kustaurant.home.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeInfoResponse(
    val topRestaurantsByRating: List<RestaurantResponse>,
    val restaurantsForMe: List<RestaurantResponse>,
    val photoUrls: List<String>,
)
