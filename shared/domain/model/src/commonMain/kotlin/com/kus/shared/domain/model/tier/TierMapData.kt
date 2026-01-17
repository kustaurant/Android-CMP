package com.kus.shared.domain.model.tier

data class TierMapData(
    val polygonCoords: List<GeoPoint>,
    val solidLines: List<List<GeoPoint>>,
    val dashedLines: List<List<GeoPoint>>,
    val favoriteTierRestaurants: List<TierRestaurant>,
    val tieredTierRestaurants: List<TierRestaurant>,
    val nonTieredRestaurants: List<NonTieredRestaurantGroup>,
    val minZoom: Int,
    val visibleBounds: List<Double>,
)