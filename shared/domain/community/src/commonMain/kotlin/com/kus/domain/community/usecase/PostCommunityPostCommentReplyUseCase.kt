package com.kus.domain.community.usecase

import com.kus.domain.community.model.CommunityPostComment
import com.kus.domain.community.repository.CommunityRepository


class PostCommunityPostCommentReplyUseCase(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(content : String, postId : Long, parentCommentId : Long?) : CommunityPostComment {
        return communityRepository.postCommunityCommentReply( postId, content, parentCommentId)
    }
}