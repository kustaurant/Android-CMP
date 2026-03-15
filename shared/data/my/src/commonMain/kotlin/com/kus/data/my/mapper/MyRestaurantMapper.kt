package com.kus.data.my.mapper

import com.kus.data.my.remote.response.EvaluatedResponse
import com.kus.data.my.remote.response.FavoriteResponse
import com.kus.shared.domain.model.my.EvaluatedResItem
import com.kus.shared.domain.model.my.FavoriteResItem

fun EvaluatedResponse.toDomain(): EvaluatedResItem =
    EvaluatedResItem(
        restaurantId = restaurantId,
        restaurantName = restaurantName,
        restaurantImgURL = restaurantImgURL,
        cuisine = cuisine,
        evaluationScore = evaluationScore,
        evaluationBody = evaluationBody,
        evaluationItemScores = evaluationItemScores,
    )

fun FavoriteResponse.toDomain(): FavoriteResItem =
    FavoriteResItem(
        restaurantName = restaurantName,
        restaurantId = restaurantId,
        restaurantImgURL = restaurantImgURL,
        mainTier = mainTier,
        restaurantType = restaurantType,
        restaurantPosition = restaurantPosition,
    )
