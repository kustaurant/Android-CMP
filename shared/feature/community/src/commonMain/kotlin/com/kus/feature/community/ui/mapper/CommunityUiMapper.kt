package com.kus.feature.community.ui.mapper

import com.kus.domain.community.model.CommunityPost
import com.kus.domain.community.model.CommunityPostComment
import com.kus.feature.community.ui.model.CommunityPostCommentUi
import com.kus.feature.community.ui.model.CommunityPostUi

fun CommunityPost.toUiModel(): CommunityPostUi = CommunityPostUi(
    postId = postId,
    category = category,
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
    comments = comments?.map { it.toUiModel() }
)

fun CommunityPostComment.toUiModel(): CommunityPostCommentUi = CommunityPostCommentUi(
    commentId = commentId,
    parentCommentId = parentCommentId,
    body = body,
    status = status,
    likeCount = likeCount,
    dislikeCount = dislikeCount,
    repliesList = repliesList.map { it.toUiModel() },
    timeAgo = timeAgo,
    reactionType = reactionType,
    isCommentMine = isCommentMine,
    writerId = writerId,
    nickname = nickname,
    evalCount = evalCount,
    writerIconUrl = writerIconUrl,
    isDeleted = false,
)