package com.kus.shared.domain.evaluate.repository

import com.kus.shared.domain.model.evaluate.PreviousEvaluation

interface EvaluateRepository {
    suspend fun getEvaluation(restaurantId: Long): PreviousEvaluation

    suspend fun postEvaluation(
        restaurantId: Long,
        evaluationScore: Double,
        evaluationSituations: List<Int>?,
        evaluationComment: String?,
        imageBytes: ByteArray?,
    )
}
