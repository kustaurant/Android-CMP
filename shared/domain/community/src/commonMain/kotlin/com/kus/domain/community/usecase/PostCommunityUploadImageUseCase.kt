package com.kus.domain.community.usecase

import com.kus.domain.community.repository.CommunityRepository


class PostCommunityUploadImageUseCase(
    private val communityRepository: CommunityRepository
){
    suspend operator fun invoke(imagePath : String) : String {
        return communityRepository.postUploadImage(imagePath)
    }
}