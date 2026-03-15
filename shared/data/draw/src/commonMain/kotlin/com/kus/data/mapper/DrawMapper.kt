package com.kus.data.mapper

import com.kus.data.draw.remote.DrawRestaurantResponse
import com.kus.domain.draw.model.DrawRestaurant

fun DrawRestaurantResponse.toDomain() : DrawRestaurant =
    DrawRestaurant(
        restaurantId = restaurantId,
        restaurantRanking = restaurantRanking,
        restaurantName = restaurantName,
        restaurantCuisine = restaurantCuisine,
        restaurantPosition = restaurantPosition,
        restaurantImgUrl = restaurantImgUrl,
        mainTier = mainTier,
        latitude = latitude,
        longitude = longitude,
        partnershipInfo = partnershipInfo,
        restaurantScore = restaurantScore,
        isEvaluated = isEvaluated,
        isFavorite = isFavorite,
        cuisineImgUrl = cuisineImgUrl,
    )