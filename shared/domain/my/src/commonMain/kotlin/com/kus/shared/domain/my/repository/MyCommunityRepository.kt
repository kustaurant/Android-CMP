package com.kus.shared.domain.my.repository

import com.kus.shared.domain.model.my.MyCommentItem
import com.kus.shared.domain.model.my.MyPostItem

interface MyCommunityRepository {
    suspend fun getMyCommunityComments() : List<MyCommentItem>
    suspend fun getMyCommunityPosts() : List<MyPostItem>
    suspend fun getMyCommunityScraps(): List<MyPostItem>
}
