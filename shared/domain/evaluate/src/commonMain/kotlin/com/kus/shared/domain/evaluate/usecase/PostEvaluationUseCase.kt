package com.kus.shared.domain.evaluate.usecase

import com.kus.shared.domain.evaluate.repository.EvaluateRepository

class PostEvaluationUseCase(
    private val evaluateRepository: EvaluateRepository,
) {
    suspend operator fun invoke(
        restaurantId: Long,
        evaluationScore: Double,
        evaluationSituations: List<Int>?,
        evaluationComment: String?,
        imageBytes: ByteArray?,
    ) {
        evaluateRepository.postEvaluation(
            restaurantId = restaurantId,
            evaluationScore = evaluationScore,
            evaluationSituations = evaluationSituations,
            evaluationComment = evaluationComment,
            imageBytes = imageBytes,
        )
    }
}
