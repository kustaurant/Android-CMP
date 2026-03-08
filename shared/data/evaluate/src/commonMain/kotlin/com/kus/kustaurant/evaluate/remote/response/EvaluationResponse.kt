package com.kus.kustaurant.evaluate.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationResponse(
    val evaluationScore: Double? = null,
    val evaluationSituations: List<Int>? = null,
    val evaluationImgUrl: String? = null,
    val evaluationComment: String? = null,
    val starComments: List<StarCommentResponse> = emptyList(),
    val newImage: String? = null,
)

@Serializable
data class StarCommentResponse(
    val star: Double,
    val comment: String,
)
