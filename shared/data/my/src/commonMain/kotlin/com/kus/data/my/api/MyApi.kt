package com.kus.data.my.api

import com.kus.data.my.remote.response.MyInfoResponse
import com.kus.data.my.remote.response.request.FeedbackRequest
import com.kus.data.my.remote.response.request.PatchProfileInfoRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MyApi(
    private val client: HttpClient,
) {
    suspend fun getMyInfo(): MyInfoResponse {
        return client.get("/api/v2/mypage").body()
    }

    suspend fun postFeedback(request: FeedbackRequest): String {
        return client.post("/api/v2/auth/mypage/feedback"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun patchProfileInfo(request: PatchProfileInfoRequest): String {
        return client.patch("/api/v2/auth/mypage/profile") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}