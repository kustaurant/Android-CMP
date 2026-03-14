package com.kus.shared.domain.evaluate.usecase

import com.kus.shared.domain.evaluate.repository.EvaluateRepository
import com.kus.shared.domain.model.evaluate.PreviousEvaluation

class GetEvaluationUseCase(
    private val evaluateRepository: EvaluateRepository,
) {
    suspend operator fun invoke(restaurantId: Long): PreviousEvaluation {
        return evaluateRepository.getEvaluation(restaurantId)
    }
}
