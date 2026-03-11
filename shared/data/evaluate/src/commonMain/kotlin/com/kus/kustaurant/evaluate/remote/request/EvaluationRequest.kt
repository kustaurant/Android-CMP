package com.kus.kustaurant.evaluate.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationRequest(
    val evaluationScore: Double,
    val evaluationSituations: List<Int>? = null,
    val evaluationComment: String? = null,
)
