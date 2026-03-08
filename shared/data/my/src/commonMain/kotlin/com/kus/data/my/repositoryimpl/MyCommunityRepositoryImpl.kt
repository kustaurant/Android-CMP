package com.kus.data.my.repositoryimpl

import com.kus.data.my.api.MyCommunityApi
import com.kus.data.my.mapper.toDomain
import com.kus.shared.domain.model.my.MyCommentItem
import com.kus.shared.domain.model.my.MyPostItem
import com.kus.shared.domain.my.repository.MyCommunityRepository

class MyCommunityRepositoryImpl(
    private val api: MyCommunityApi,
) : MyCommunityRepository {
    override suspend fun getMyCommunityComments(): List<MyCommentItem> =
        api.getMyCommunityComments().map { it.toDomain() }

    override suspend fun getMyCommunityPosts(): List<MyPostItem> =
        api.getMyCommunityPosts().map { it.toDomain() }

    override suspend fun getMyCommunityScraps(): List<MyPostItem> =
        api.getMyCommunityScraps().map { it.toDomain() }

}
