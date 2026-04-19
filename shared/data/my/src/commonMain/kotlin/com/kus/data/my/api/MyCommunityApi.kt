package com.kus.data.my.api

import com.kus.data.my.remote.response.MyCommentResponse
import com.kus.data.my.remote.response.MyPostResponse
import com.kus.data.network.ApiClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

class MyCommunityApi(
    private val apiClientProvider: ApiClientProvider,
) {
    private val client get() = apiClientProvider.client

    suspend fun getMyCommunityComments(): List<MyCommentResponse> {
        return client.get("/api/v2/auth/mypage/community/comments").body()
    }

    suspend fun getMyCommunityPosts(): List<MyPostResponse> {
        return client.get("/api/v2/auth/mypage/community/posts").body()
    }

    suspend fun getMyCommunityScraps(): List<MyPostResponse> {
        return client.get("/api/v2/auth/mypage/community/scraps").body()
    }
}
