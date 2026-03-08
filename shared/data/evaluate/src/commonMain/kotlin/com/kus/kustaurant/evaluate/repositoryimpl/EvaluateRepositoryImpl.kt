package com.kus.kustaurant.evaluate.repositoryimpl

import com.kus.kustaurant.evaluate.api.EvaluateApi
import com.kus.kustaurant.evaluate.remote.mapper.toDomain
import com.kus.kustaurant.evaluate.remote.request.EvaluationRequest
import com.kus.shared.domain.evaluate.repository.EvaluateRepository
import com.kus.shared.domain.model.evaluate.PreviousEvaluation

class EvaluateRepositoryImpl(
    private val api: EvaluateApi,
) : EvaluateRepository {
    override suspend fun getEvaluation(restaurantId: Long): PreviousEvaluation =
        api.getEvaluation(restaurantId).toDomain()

    override suspend fun postEvaluation(
        restaurantId: Long,
        evaluationScore: Double,
        evaluationSituations: List<Int>?,
        evaluationComment: String?,
        imageBytes: ByteArray?,
    ) {
        val request = EvaluationRequest(
            evaluationScore = evaluationScore,
            evaluationSituations = evaluationSituations?.takeIf { it.isNotEmpty() },
            evaluationComment = evaluationComment?.takeIf { it.isNotBlank() },
        )
        api.postEvaluation(
            restaurantId = restaurantId,
            request = request,
            imageBytes = imageBytes,
        )
    }
}
