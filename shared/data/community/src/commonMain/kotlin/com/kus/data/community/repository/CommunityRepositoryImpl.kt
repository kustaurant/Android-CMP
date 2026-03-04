package com.kus.data.community.repository

import com.kus.data.community.PlatformImageResolver
import com.kus.data.community.api.CommunityApi
import com.kus.data.community.mapper.toDomain
import com.kus.data.community.remote.request.PostCommentRequest
import com.kus.data.community.remote.request.PostRequest
import com.kus.data.community.remote.response.toDomain
import com.kus.domain.auth.repository.DeviceIdManager
import com.kus.domain.community.model.AuthUserInfo
import com.kus.domain.community.model.CommunityCommentReaction
import com.kus.domain.community.model.CommunityPost
import com.kus.domain.community.model.CommunityPostComment
import com.kus.domain.community.model.CommunityPostLike
import com.kus.domain.community.model.CommunityPostListItem
import com.kus.domain.community.model.CommunityPostScrap
import com.kus.domain.community.model.CommunityRanking
import com.kus.domain.community.model.LikeEvent
import com.kus.domain.community.model.ListSortType
import com.kus.domain.community.model.PostCategory
import com.kus.domain.community.model.PostModification
import com.kus.domain.community.model.RankingSortType
import com.kus.domain.community.repository.CommunityRepository

class CommunityRepositoryImpl(
    private val communityApi: CommunityApi,
    private val deviceIdManager: DeviceIdManager,
    private val resolver: PlatformImageResolver,
) : CommunityRepository {

    override suspend fun postUploadImage(imagePath: String): String {
        val resolved = resolver.resolve(imagePath)
        return communityApi.postCommunityUploadImage(
            imageBytes = resolved.bytes,
            fileName = resolved.fileName,
        ).imgUrl
    }

    override suspend fun getPostList(
        postCategory: PostCategory,
        page: Int,
        sort: ListSortType
    ): List<CommunityPostListItem> {
        return communityApi.getCommunityPostListData(
            category = postCategory.value,
            page = page,
            sort = sort.value
        ).map{it.toDomain()}
    }

    override suspend fun getRankingList(sort: RankingSortType): List<CommunityRanking> {
        return communityApi.getCommunityRankingListData(sort.value).map {it.toDomain()}
    }

    override suspend fun getPostDetail(postId: Long, isLoggedIn: Boolean): CommunityPost {
        val deviceId = if (isLoggedIn) null else deviceIdManager.getOrCreateDeviceId()
        return communityApi.getCommunityPostDetailData(postId, deviceId).toDomain()
    }

    override suspend fun postPostCreate(
        title: String,
        postCategory: PostCategory,
        content: String
    ): CommunityPost {
        val request = PostRequest(title, postCategory.value, content)
        return communityApi.postPostCreate(request).toDomain()
    }

    override suspend fun postPostDetailScrap(postId: Long, scrapped: Boolean): CommunityPostScrap {
        return communityApi.postCommunityPostDetailScrap(postId, scrapped).toDomain()
    }

    override suspend fun postPostLikeToggle(postId: Long, likeEvent: LikeEvent?): CommunityPostLike {
        return communityApi.postCommunityPostLikeToggle(postId, likeEvent?.value).toDomain()
    }

    override suspend fun patchPostModify(
        postId: String,
        title: String,
        postCategory: PostCategory,
        content: String
    ) {
        val request = PostRequest(title, postCategory.value, content)
        communityApi.patchModifyPost(postId, request)
    }

    override suspend fun getPostModify(
        postId: Long,
        user: AuthUserInfo,
    ): PostModification {
        return communityApi
            .getModifyPost(postId = postId, user = user)
            .toDomain()
    }

    override suspend fun postCommunityCommentReply(
        postId: Long,
        content: String,
        parentCommentId: Long?
    ): CommunityPostComment {
        val request = PostCommentRequest(content, parentCommentId)
        return communityApi.postCommunityPostCommentReply(postId, request).toDomain()
    }

    override suspend fun postCommentLikeToggle(
        commentId: Long,
        reaction: LikeEvent?
    ): CommunityCommentReaction {
        return communityApi.putCommentLikeToggle(commentId, reaction?.value).toDomain()
    }

    override suspend fun deletePost(postId: Long) {
        communityApi.deletePost(postId)
    }

    override suspend fun deleteCommunityComment(commentId: Long) {
        communityApi.deletePostComment(commentId)
    }
}
