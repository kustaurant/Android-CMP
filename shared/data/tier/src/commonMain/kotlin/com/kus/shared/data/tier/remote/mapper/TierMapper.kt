package com.kus.shared.data.tier.remote.mapper

import com.kus.shared.data.tier.remote.response.GeoPointResponse
import com.kus.shared.data.tier.remote.response.NonTieredRestaurantsResponse
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

fun NonTieredRestaurantsResponse.toDomain(): NonTieredRestaurantGroup =
    NonTieredRestaurantGroup(
        zoom = zoom,
        tierRestaurants = restaurants.map {
            TierRestaurant(
                restaurantId = it.restaurantId,
                restaurantRanking = it.restaurantRanking ?: 0,
                restaurantName = it.restaurantName,
                restaurantCuisine = it.restaurantCuisine,
                restaurantPosition = it.restaurantPosition,
                restaurantImgUrl = it.restaurantImgUrl,
                mainTier = -1,
                isEvaluated = it.isEvaluated,
                isFavorite = it.isFavorite,
                longitude = it.longitude,
                latitude = it.latitude,
                partnershipInfo = it.partnershipInfo ?: "",
                restaurantScore = it.restaurantScore?.takeIf { s -> !s.isNaN() } ?: 0.0,
                isTempTier = it.isTempTier,
            )
        }
    )

fun TierMapDataResponse.toDomain(): TierMapData {
    return TierMapData(
        solidPolygonCoordsList = solidPolygonCoordsList.map { it.toDomainClosed() },
        dashedPolygonCoordsList = dashedPolygonCoordsList.map { it.toDomainClosed() },

        favoriteTierRestaurants = favoriteRestaurants.map { it.toDomain() },
        tieredTierRestaurants = tieredRestaurants.map { it.toDomain() },
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
        longitude = longitude,
        latitude = latitude,
    )
