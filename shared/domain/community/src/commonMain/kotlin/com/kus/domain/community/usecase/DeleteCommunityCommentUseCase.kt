package com.kus.domain.community.usecase

import com.kus.domain.community.repository.CommunityRepository


class DeleteCommunityCommentUseCase(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(commentId : Long) {
        return communityRepository.deleteCommunityComment(commentId)
    }
}