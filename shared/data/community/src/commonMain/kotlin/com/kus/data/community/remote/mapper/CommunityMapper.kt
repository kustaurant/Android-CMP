package com.kus.data.community.remote.mapper

import com.kus.data.community.remote.response.CommunityCommentReactionResponse
import com.kus.data.community.remote.response.CommunityPostCommentResponse
import com.kus.data.community.remote.response.CommunityPostLikeResponse
import com.kus.data.community.remote.response.CommunityPostListItemResponse
import com.kus.data.community.remote.response.CommunityPostResponse
import com.kus.data.community.remote.response.CommunityPostScrapResponse
import com.kus.data.community.remote.response.CommunityRankingResponse
import com.kus.data.community.remote.response.PostResponse
import com.kus.domain.community.model.CommunityCommentReaction
import com.kus.domain.community.model.CommunityPost
import com.kus.domain.community.model.CommunityPostComment
import com.kus.domain.community.model.CommunityPostLike
import com.kus.domain.community.model.CommunityPostListItem
import com.kus.domain.community.model.CommunityPostScrap
import com.kus.domain.community.model.CommunityRanking
import com.kus.domain.community.model.PostModification

fun CommunityPostResponse.toDomain(): CommunityPost =
    CommunityPost(
        postId = postId,
        category = category.toDomain(),
        title = title,
        body = body,
        photoUrls = photoUrls,
        writerId = writerId,
        nickname = nickname,
        evalCount = evalCount,
        writerIconUrl = writerIconUrl,
        timeAgo = timeAgo,
        createdAt = createdAt,
        updatedAt = updatedAt,
        likeOnlyCount = likeOnlyCount,
        dislikeOnlyCount = dislikeOnlyCount,
        totalLikes = totalLikes,
        commentCount = commentCount,
        scrapCount = scrapCount,
        visitCount = visitCount,
        myReaction = myReaction,
        isScrapped = isScrapped,
        isPostMine = isPostMine,
        comments = comments?.map { it.toDomain() }.orEmpty()
    )

fun PostResponse.toDomain() : PostModification {
    return PostModification(
        postId = postId,
        category = category.toDomain(),
        title = title,
        body = body,
        photoUrls = photoUrls
    )
}

fun CommunityPostCommentResponse.toDomain(): CommunityPostComment =
    CommunityPostComment(
        commentId = commentId,
        parentCommentId = parentCommentId,
        body = body,
        status = status,
        likeCount = likeCount,
        dislikeCount = dislikeCount,
        repliesList = replies?.map { it.toDomain() }.orEmpty(),
        timeAgo = timeAgo,
        reactionType = reactionType,
        isCommentMine = isCommentMine,
        writerId = writeruserId,
        nickname = writernickname,
        evalCount = writerevalCount,
        writerIconUrl = writericonUrl,
    )

fun CommunityCommentReactionResponse.toDomain(): CommunityCommentReaction =
    CommunityCommentReaction(
        likeCount = likeCount,
        dislikeCount = dislikeCount,
        reactionType = reactionType
    )

fun CommunityPostScrapResponse.toDomain(): CommunityPostScrap =
    CommunityPostScrap(
        postScrapCount = postScrapCount,
        isScrapped = isScrapped
    )

fun CommunityPostLikeResponse.toDomain(): CommunityPostLike =
    CommunityPostLike(
        reactionType = reactionType,
        likeCount = likeCount,
        dislikeCount = dislikeCount,
        netLikes = netLikes
    )

fun CommunityPostListItemResponse.toDomain(): CommunityPostListItem =
    CommunityPostListItem(
        postId = postId,
        category = category,
        title = title,
        body = body,
        writerId = writerId,
        nickname = nickname,
        evalCount = evalCount,
        writerIconUrl = writerIconUrl,
        photoUrl = photoUrl,
        timeAgo = timeAgo,
        totalLikes = totalLikes,
        commentCount = commentCount
    )

fun CommunityRankingResponse.toDomain() : CommunityRanking =
    CommunityRanking(
        userId = userId,
        nickname = nickname,
        iconUrl = iconUrl,
        evaluationCount = evaluationCount,
        rank = rank
    )