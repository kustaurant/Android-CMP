package com.kus.shared.domain.detail.usecase

import com.kus.shared.domain.detail.repository.DetailRepository
import com.kus.shared.domain.model.detail.ReactionResult

class PutEvaluationReactionUseCase(
    private val detailRepository: DetailRepository,
) {
    suspend operator fun invoke(
        evalCommentId: Int,
        reaction: String?,
    ): ReactionResult {
        return detailRepository.putEvaluationReaction(evalCommentId, reaction)
    }
}
