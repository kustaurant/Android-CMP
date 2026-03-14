package com.kus.data.community.api

import com.kus.data.community.remote.request.PostCommentRequest
import com.kus.data.community.remote.request.PostRequest
import com.kus.data.community.remote.response.CommunityCommentReactionResponse
import com.kus.data.community.remote.response.CommunityCreatePostResponse
import com.kus.data.community.remote.response.CommunityPostCommentResponse
import com.kus.data.community.remote.response.CommunityPostLikeResponse
import com.kus.data.community.remote.response.CommunityPostListItemResponse
import com.kus.data.community.remote.response.CommunityPostResponse
import com.kus.data.community.remote.response.CommunityPostScrapResponse
import com.kus.data.community.remote.response.CommunityPostUploadImageResponse
import com.kus.data.community.remote.response.CommunityRankingResponse
import com.kus.data.community.remote.response.PostResponse
import com.kus.domain.community.model.AuthUserInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class CommunityApi(
    private val client: HttpClient,
) {
    suspend fun getCommunityPostDetailData(
        postId: Long,
        deviceId: String?
    ): CommunityPostResponse =
        client.get("/api/v2/community/$postId") {
            deviceId?.let {
                headers { append("X-device-id", it) }
            }
        }.body()

    suspend fun getCommunityPostListData(
        category: String,
        page: Int,
        sort: String
    ): List<CommunityPostListItemResponse> =
        client.get("/api/v2/community/posts") {
            parameter("category", category)
            parameter("page", page)
            parameter("sort", sort)
        }.body()

    suspend fun deletePostComment(commentId: Long) {
        client.delete("/api/v2/auth/comments/$commentId")
    }

    suspend fun postCommunityPostCommentReply(
        postId: Long,
        postCommentRequest: PostCommentRequest
    ): CommunityPostCommentResponse =
        client.post("/api/v2/auth/posts/$postId/comments") {
            contentType(ContentType.Application.Json)
            setBody(postCommentRequest)
        }.body()

    suspend fun putCommentLikeToggle(
        commentId: Long,
        reaction: String?
    ): CommunityCommentReactionResponse =
        client.put("/api/v2/auth/community/comments/$commentId/reaction") {
            // reaction이 null이면 쿼리를 아예 빼는게 안전
            if (reaction != null) parameter("reaction", reaction)
        }.body()

    suspend fun postPostCreate(
        postRequest: PostRequest
    ): CommunityCreatePostResponse =
        client.post("/api/v2/auth/community/posts") {
            contentType(ContentType.Application.Json)
            setBody(postRequest)
        }.body()

    suspend fun patchModifyPost(
        postId: String,
        postRequest: PostRequest
    ) {
        client.patch("/api/v2/auth/community/posts/$postId") {
            contentType(ContentType.Application.Json)
            setBody(postRequest)
        }
    }

    suspend fun getModifyPost(
        postId: Long,
        user: AuthUserInfo,
    ): PostResponse =
        client.get("/api/v2/auth/community/posts/$postId") {
            parameter("id", user.id)
            parameter("role", user.role)  
        }.body()

    suspend fun deletePost(postId: Long) {
        client.delete("/api/v2/auth/community/posts/$postId")
    }

    suspend fun postCommunityUploadImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String = "image/jpeg",
    ): CommunityPostUploadImageResponse {
        val response = client.submitFormWithBinaryData(
            url = "/api/v2/auth/community/posts/image",
            formData = formData {
                append(
                    key = "image",
                    value = imageBytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, mimeType)
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    }
                )
            }
        )

        if (!response.status.isSuccess()) {
            val err = response.bodyAsText() // 에러 JSON 그대로
            throw IllegalStateException("Upload failed: HTTP ${response.status.value} ${response.status.description} body=$err")
        }

        return response.body()
    }

    suspend fun postCommunityPostLikeToggle(
        postId: Long,
        cmd: String?
    ): CommunityPostLikeResponse =
        client.put("/api/v2/auth/community/$postId/reaction") {
            if (cmd != null) parameter("cmd", cmd)
        }.body()

    suspend fun postCommunityPostDetailScrap(
        postId: Long,
        scrapped: Boolean
    ): CommunityPostScrapResponse =
        client.post("/api/v2/auth/community/$postId/scraps") {
            parameter("scrapped", scrapped)
        }.body()

    suspend fun getCommunityRankingListData(sort: String): List<CommunityRankingResponse> =
        client.get("/api/v2/community/ranking") {
            parameter("sort", sort)
        }.body()
}