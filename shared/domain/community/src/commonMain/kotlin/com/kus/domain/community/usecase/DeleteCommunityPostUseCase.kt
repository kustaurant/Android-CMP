package com.kus.domain.community.usecase

import com.kus.domain.community.repository.CommunityRepository

class DeleteCommunityPostUseCase(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(postId : Long) {
        communityRepository.deletePost(postId)
    }
}