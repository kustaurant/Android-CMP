package com.kus.shared.domain.detail.usecase

import com.kus.shared.domain.detail.repository.DetailRepository
import com.kus.shared.domain.model.detail.ReviewComment

class PostCommentUseCase(
    private val detailRepository: DetailRepository,
) {
    suspend operator fun invoke(
        restaurantId: Long,
        evalId: Int,
        body: String,
    ): ReviewComment {
        return detailRepository.postComment(restaurantId, evalId, body)
    }
}
