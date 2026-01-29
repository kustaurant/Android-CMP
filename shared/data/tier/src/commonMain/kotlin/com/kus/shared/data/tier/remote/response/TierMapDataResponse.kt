package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class TierMapDataResponse(
    val polygonCoords: List<GeoPointResponse> = emptyList(),
    val solidLines: List<List<GeoPointResponse>> = emptyList(),
    val dashedLines: List<List<GeoPointResponse>> = emptyList(),
    val favoriteTierRestaurants: List<RestaurantResponse> = emptyList(),
    val tieredTierRestaurants: List<RestaurantResponse> = emptyList(),
    val nonTieredRestaurants: List<NonTieredRestaurantGroupResponse> = emptyList(),
    val minZoom: Int = 0,
    val visibleBounds: List<Double> = emptyList(),
)