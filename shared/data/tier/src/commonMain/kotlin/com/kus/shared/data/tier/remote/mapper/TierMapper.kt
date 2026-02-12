package com.kus.shared.data.tier.remote.mapper

import com.kus.shared.data.tier.remote.response.GeoPointResponse
import com.kus.shared.data.tier.remote.response.NonTieredRestaurantGroupResponse
import com.kus.shared.data.tier.remote.response.RestaurantResponse
import com.kus.shared.data.tier.remote.response.TierMapDataResponse
import com.kus.shared.domain.model.tier.GeoPoint
import com.kus.shared.domain.model.tier.NonTieredRestaurantGroup
import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant

 
fun RestaurantResponse.toDomain(): TierRestaurant =
    TierRestaurant(
        restaurantId = restaurantId,
        restaurantRanking = restaurantRanking ?: 0,
        restaurantName = restaurantName,
        restaurantCuisine = restaurantCuisine,
        restaurantPosition = restaurantPosition,
        restaurantImgUrl = restaurantImgUrl,
        mainTier = mainTier,
        isEvaluated = isEvaluated,
        isFavorite = isFavorite,
        longitude = longitude,
        latitude = latitude,
        partnershipInfo = partnershipInfo ?: "",
        restaurantScore = restaurantScore?.takeIf { s -> !s.isNaN() } ?: 0.0,
        isTempTier = isTempTier,
    )

fun NonTieredRestaurantGroupResponse.toDomain(): NonTieredRestaurantGroup =
    NonTieredRestaurantGroup(
        zoom = zoom,
        tierRestaurants = tierRestaurants.map { it.toDomain() }
    )

fun TierMapDataResponse.toDomain(): TierMapData {
    return TierMapData(
        solidPolygonCoordsList = solidLines.map { it.toDomainClosed() },
        dashedPolygonCoordsList = dashedLines.map { it.toDomainClosed() },

        favoriteTierRestaurants = favoriteTierRestaurants.map { it.toDomain() },
        tieredTierRestaurants = tieredTierRestaurants.map { it.toDomain() },
        nonTieredRestaurants = nonTieredRestaurants.map { it.toDomain() },

        minZoom = minZoom,
        visibleBounds = visibleBounds,
    )
}

private fun List<GeoPointResponse>.toDomainClosed(): List<GeoPoint> {
    if (isEmpty()) return emptyList()

    val mapped = map { it.toDomain() }
    val first = mapped.first()
    val last = mapped.last()

    return if (first != last) mapped + first else mapped
}

fun GeoPointResponse.toDomain(): GeoPoint =
    GeoPoint(
        latitude = y,
        longitude = x
    )
