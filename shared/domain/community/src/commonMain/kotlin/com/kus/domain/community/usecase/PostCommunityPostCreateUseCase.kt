package com.kus.domain.community.usecase

import com.kus.domain.community.model.CommunityPost
import com.kus.domain.community.model.PostCategory
import com.kus.domain.community.repository.CommunityRepository


class PostCommunityPostCreateUseCase(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(title : String, postCategory : PostCategory, content : String) : CommunityPost {
        return communityRepository.postPostCreate(title, postCategory, content)
    }
}