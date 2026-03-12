package com.kus.domain.community.usecase

import com.kus.domain.community.repository.CommunityRepository
import com.kus.domain.community.model.PostCategory


class PatchPostModifyUseCase(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(postId: String, title : String, postCategory : PostCategory, content : String,) {
        return communityRepository.patchPostModify(postId, title, postCategory, content)
    }
}