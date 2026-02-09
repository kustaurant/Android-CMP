package com.kus.data.auth.api

import com.kus.data.auth.model.AuthTokens
import com.kus.data.auth.remote.response.RefreshResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
 
class AuthRefreshApi(
    private val basicClient: HttpClient,
    private val baseUrl: String,
){
    suspend fun refresh(refreshToken: String): AuthTokens {
        val res: RefreshResponse =
            basicClient.post("$baseUrl/api/v2/token/refresh") {
                contentType(ContentType.Application.Json)
                setBody("{}")
                header(HttpHeaders.Authorization, "Bearer $refreshToken")
            }.body()

        return AuthTokens(
            accessToken = res.accessToken,
            refreshToken = res.refreshToken,
        )
    }
}