package com.kus.shared.data.search.mapper

import com.kus.shared.data.search.remote.response.RestaurantResponse
import com.kus.shared.domain.model.restaurant.RestaurantItem

fun RestaurantResponse.toDomain(): RestaurantItem =
    RestaurantItem(
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
        tierImgUrl = tierImgUrl,
        cuisineImgUrl = cuisineImgUrl,
        isTempTier = isTempTier,
    )
