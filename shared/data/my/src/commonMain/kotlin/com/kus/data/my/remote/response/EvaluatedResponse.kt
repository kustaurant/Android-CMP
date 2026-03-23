package com.kus.data.my.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class EvaluatedResponse(
    val restaurantId: Int,
    val restaurantName: String,
    val restaurantImgURL: String,
    val cuisine: String,
    val evaluationScore: Double,
    val evaluationBody: String?,
    val evaluationItemScores: List<String>?,
)
