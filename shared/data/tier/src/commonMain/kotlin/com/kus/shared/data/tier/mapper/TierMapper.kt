package com.kus.shared.data.tier.mapper

import com.kus.shared.data.tier.remote.response.GeoPointResponse
import com.kus.shared.data.tier.remote.response.NonTieredRestaurantsResponse
import com.kus.shared.data.tier.remote.response.RestaurantResponse
import com.kus.shared.data.tier.remote.response.TierMapDataResponse
import com.kus.shared.domain.model.tier.GeoPoint
import com.kus.shared.domain.model.tier.NonTieredRestaurantGroup
import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation

fun Cuisine.toApiCode(): String = when (this) {
    Cuisine.ALL -> "ALL"
    Cuisine.KOREAN -> "KO"
    Cuisine.JAPANESE -> "JA"
    Cuisine.CHINESE -> "CH"
    Cuisine.WESTERN -> "WE"
    Cuisine.ASIAN -> "AS"
    Cuisine.MEAT -> "ME"
    Cuisine.CHICKEN -> "CK"
    Cuisine.SEAFOOD -> "SE"
    Cuisine.BURGER_PIZZA -> "HP"
    Cuisine.BUNSIK -> "BS"
    Cuisine.PUB -> "PU"
    Cuisine.CAFE_DESSERT -> "CA"
    Cuisine.BAKERY -> "BA"
    Cuisine.SALAD -> "SA"
    Cuisine.PARTNERSHIP -> "JH"
}

fun Situation.toApiCode(): String = when (this) {
    Situation.ALL -> "ALL"
    Situation.SOLO -> "1"
    Situation.TWO_TO_FOUR -> "2"
    Situation.FIVE_OR_MORE -> "3"
    Situation.COMPANY_DINNER -> "4"
    Situation.DELIVERY -> "5"
    Situation.LATE_NIGHT -> "6"
    Situation.INVITE_FRIENDS -> "7"
    Situation.DATE -> "8"
    Situation.BLIND_DATE -> "9"
}

fun Location.toApiCode(): String = when (this) {
    Location.ALL -> "ALL"
    Location.KONKUK_TO_JUNGMUN -> "L1"
    Location.JUNGMUN_TO_EODAE -> "L2"
    Location.BACK_GATE -> "L3"
    Location.FRONT_GATE -> "L4"
    Location.GUUI_STATION -> "L5"
}

fun Set<Cuisine>.toCuisineQuery(): String =
    Cuisine.normalize(this).joinToString(",") { it.toApiCode() }

fun Set<Situation>.toSituationQuery(): String =
    Situation.normalize(this).joinToString(",") { it.toApiCode() }

fun Set<Location>.toLocationQuery(): String =
    Location.normalize(this).joinToString(",") { it.toApiCode() }


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
        partnershipInfo = partnerships?.joinToString("\n")?.takeIf { it.isNotEmpty() },
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
                partnershipInfo = it.partnerships?.joinToString("\n")?.takeIf { it.isNotEmpty() },
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
        visibleBounds = visibleBounds?.let {
            listOf(it.minLng, it.maxLng, it.minLat, it.maxLat)
        } ?: emptyList(),
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
