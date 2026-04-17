package com.kus.domain.community.repository

import com.kus.domain.community.model.AuthUserInfo
import com.kus.domain.community.model.CommunityCommentReaction
import com.kus.domain.community.model.ListSortType
import com.kus.domain.community.model.CommunityPost
import com.kus.domain.community.model.CommunityPostComment
import com.kus.domain.community.model.CommunityPostLike
import com.kus.domain.community.model.CommunityPostListItem
import com.kus.domain.community.model.CommunityPostScrap
import com.kus.domain.community.model.CommunityRanking
import com.kus.domain.community.model.LikeEvent
import com.kus.domain.community.model.PostModification
import com.kus.domain.community.model.RankingSortType
import com.kus.domain.community.model.PostCategory

interface CommunityRepository {
    suspend fun getPostList(
        postCategory: PostCategory,
        page: Int,
        sort: ListSortType
    ): List<CommunityPostListItem>

    suspend fun getRankingList(
        sort: RankingSortType
    ): List<CommunityRanking>

    suspend fun getPostDetail(
        postId: Long,
        isLoggedIn : Boolean
    ): CommunityPost

    suspend fun postPostCreate(
        title : String,
        postCategory : PostCategory,
        content : String,
    ) : CommunityPost

    suspend fun postUploadImage(
        imageBytes: ByteArray
    ) : String

    suspend fun postPostDetailScrap(
        postId : Long,
        scrapped : Boolean
    ) : CommunityPostScrap

    suspend fun postPostLikeToggle(
        postId : Long,
        likeEvent : LikeEvent?,
    ) : CommunityPostLike

    suspend fun patchPostModify(
        postId: String,
        title : String,
        postCategory : PostCategory,
        content : String,
    )

    suspend fun getPostModify(
        postId: Long,
        user : AuthUserInfo,
    ) : PostModification

    suspend fun postCommunityCommentReply(
        postId : Long,
        content : String,
        parentCommentId : Long?,
    ) : CommunityPostComment

    suspend fun postCommentLikeToggle(
        commentId : Long,
        reaction : LikeEvent?,
    ) : CommunityCommentReaction

    suspend fun deletePost(
        postId : Long,
    )

    suspend fun deleteCommunityComment(
        commentId : Long,
    )
}


