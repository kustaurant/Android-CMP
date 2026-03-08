package com.kus.shared.domain.my.usecase

import com.kus.shared.domain.model.my.MyCommentItem
import com.kus.shared.domain.my.repository.MyCommunityRepository

class GetMyCommentsUseCase(
    private val repository: MyCommunityRepository,
) {
    suspend operator fun invoke(): List<MyCommentItem> {
        return repository.getMyCommunityComments()
    }
}