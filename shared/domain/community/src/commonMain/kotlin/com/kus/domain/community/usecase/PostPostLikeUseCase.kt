package com.kus.domain.community.usecase

import com.kus.domain.community.model.CommunityPostLike
import com.kus.domain.community.model.LikeEvent
import com.kus.domain.community.repository.CommunityRepository

class PostPostLikeUseCase(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(postId : Long, likeEvent : LikeEvent?) : CommunityPostLike {
        return communityRepository.postPostLikeToggle(postId, likeEvent)
    }
}
