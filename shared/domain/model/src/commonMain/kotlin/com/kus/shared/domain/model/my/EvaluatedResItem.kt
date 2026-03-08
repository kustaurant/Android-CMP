package com.kus.shared.domain.model.my

data class EvaluatedResItem(
    val restaurantId: String,
    val restaurantName: String,
    val restaurantImgURL: String,
    val cuisine: String,
    val evaluationScore: Double,
    val evaluationBody: String,
    val evaluationItemScores: List<String>,
)
