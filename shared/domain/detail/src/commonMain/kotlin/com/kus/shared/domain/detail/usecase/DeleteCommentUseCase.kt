package com.kus.shared.domain.detail.usecase

import com.kus.shared.domain.detail.repository.DetailRepository

class DeleteCommentUseCase(
    private val detailRepository: DetailRepository,
) {
    suspend operator fun invoke(
        restaurantId: Long,
        evalCommentId: Int,
    ) {
        return detailRepository.deleteComment(restaurantId, evalCommentId)
    }
}
