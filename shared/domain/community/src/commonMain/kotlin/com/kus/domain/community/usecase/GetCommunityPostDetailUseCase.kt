package com.kus.domain.community.usecase


import com.kus.domain.community.model.CommunityPost
import com.kus.domain.community.model.CommunityPostComment
import com.kus.domain.community.repository.CommunityRepository

class GetCommunityPostDetailUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postId: Long, isLoggedIn: Boolean): CommunityPost {
        val post = communityRepository.getPostDetail(postId, isLoggedIn)

        val safeComments = post.comments?.map { comment ->
            normalizeComment(comment)
        } ?: emptyList()

        return post.copy(comments = safeComments)
    }

    private fun normalizeComment(comment: CommunityPostComment): CommunityPostComment {
        val safeReplies = comment.repliesList.map { normalizeComment(it) }
        return comment.copy(repliesList = safeReplies)
    }
}

