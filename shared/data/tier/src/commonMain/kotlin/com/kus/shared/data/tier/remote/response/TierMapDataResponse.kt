package com.kus.shared.data.tier.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class TierMapDataResponse(
    val solidPolygonCoordsList: List<List<GeoPointResponse>> = emptyList(),
    val dashedPolygonCoordsList: List<List<GeoPointResponse>> = emptyList(),
    val favoriteRestaurants: List<RestaurantResponse> = emptyList(),
    val tieredRestaurants: List<RestaurantResponse> = emptyList(),
    val nonTieredRestaurants: List<NonTieredRestaurantsResponse> = emptyList(),
    val minZoom: Int = 0,
    val visibleBounds: VisibleBoundsResponse? = null,
)


