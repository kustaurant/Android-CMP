package com.kus.domain.community.usecase

import com.kus.domain.community.model.ListSortType
import com.kus.domain.community.model.CommunityPostListItem
import com.kus.domain.community.repository.CommunityRepository
import com.kus.domain.community.model.PostCategory

class GetCommunityPostListUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(postCategory: PostCategory, page: Int, sort: ListSortType): List<CommunityPostListItem> {
        return communityRepository.getPostList(postCategory, page, sort)
    }
}