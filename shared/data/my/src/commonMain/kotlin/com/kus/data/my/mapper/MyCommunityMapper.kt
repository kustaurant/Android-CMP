package com.kus.data.my.mapper

import com.kus.data.my.remote.response.MyCommentResponse
import com.kus.data.my.remote.response.MyPostResponse
import com.kus.shared.domain.model.my.MyCommentItem
import com.kus.shared.domain.model.my.MyPostItem

fun MyCommentResponse.toDomain(): MyCommentItem =
    MyCommentItem(
        postId = postId,
        postCategory = postCategory,
        postTitle = postTitle,
        body = body,
        likeCount = likeCount,
        timeAgo = timeAgo,
    )

fun MyPostResponse.toDomain(): MyPostItem =
    MyPostItem(
        postId = postId,
        postCategory = postCategory,
        postTitle = postTitle,
        postImgUrl = postImgUrl,
        fullBody = fullBody,
        likeCount = likeCount,
        commentCount = commentCount,
        timeAgo = timeAgo,
        body = body,
    )
