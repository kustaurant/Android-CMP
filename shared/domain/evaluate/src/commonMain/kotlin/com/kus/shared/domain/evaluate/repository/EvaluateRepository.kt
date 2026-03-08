package com.kus.shared.domain.evaluate.repository

interface EvaluateRepository {
    suspend fun postEvaluation(
        restaurantId: Long,
        evaluationScore: Double,
        evaluationSituations: List<Int>?,
        evaluationComment: String?,
        imageBytes: ByteArray?,
    )
}
