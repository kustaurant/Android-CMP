package com.kus.domain.community.usecase

import com.kus.domain.community.model.CommunityPostScrap
import com.kus.domain.community.repository.CommunityRepository

class PostCommunityPostDetailScrapUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Long, scrapped : Boolean): CommunityPostScrap {
        return communityRepository.postPostDetailScrap(postId, scrapped)
    }
}