package com.kus.domain.community.usecase

import com.kus.domain.community.model.CommunityCommentReaction
import com.kus.domain.community.model.LikeEvent
import com.kus.domain.community.repository.CommunityRepository

class PostCommunityPostCommentReactUseCase(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(commentId : Long, reaction : LikeEvent?) : CommunityCommentReaction {
        return communityRepository.postCommentLikeToggle(commentId, reaction)
    }
}