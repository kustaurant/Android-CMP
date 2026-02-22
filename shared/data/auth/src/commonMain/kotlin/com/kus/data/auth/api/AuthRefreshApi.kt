package com.kus.data.auth.api

import com.kus.data.auth.remote.response.RefreshResponse
import com.kus.domain.auth.model.AuthToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class AuthRefreshApi(
    private val basicClient: HttpClient,
    private val baseUrl: String,
){
    suspend fun refresh(refreshToken: String): AuthToken {
        val res: RefreshResponse =
            basicClient.post("$baseUrl/api/v2/token/refresh") {
                contentType(ContentType.Application.Json)
                setBody("{}")
                header(HttpHeaders.Authorization, "Bearer $refreshToken")
            }.body()

        return AuthToken(
            accessToken = res.accessToken,
            refreshToken = res.refreshToken,
        )
    }
}