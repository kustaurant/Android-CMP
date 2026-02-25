package com.kus.kustaurant.home.remote.mapper

import com.kus.kustaurant.home.remote.response.HomeInfoResponse
import com.kus.kustaurant.home.remote.response.RestaurantResponse
import com.kus.shared.domain.model.home.HomeInfo
import com.kus.shared.domain.model.restaurant.RestaurantItem

fun HomeInfoResponse.toDomain(): HomeInfo =
    HomeInfo(
        topRestaurantsByRating = topRestaurantsByRating.map { it.toDomain() },
        restaurantsForMe = restaurantsForMe.map { it.toDomain() },
        photoUrls = photoUrls,
    )

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
        cuisineImgUrl = cuisineImgUrl,
        tierImgUrl = tierImgUrl,
        isTempTier = isTempTier,
    )
