package com.kus.data.my.api

import com.kus.data.my.remote.response.MyInfoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

class MyApi(
    private val client: HttpClient,
) {
    suspend fun getMyInfo() : MyInfoResponse {
        return client.get("/api/v2/mypage").body()
    }

    suspend fun postFeedback(content: String): String {
        return client.post("/api/v2/auth/mypage/feedback").body()
    }
}