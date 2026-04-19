package com.kus.data.auth.api

import com.kus.data.auth.remote.request.NaverLoginRequest
import com.kus.data.auth.remote.response.LoginResponse
import com.kus.data.network.ApiClientProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthApi(
    private val basicClient: HttpClient,
    private val apiClientProvider: ApiClientProvider,
    private val baseUrl: String,
) {
    private val apiClient get() = apiClientProvider.client

    suspend fun postNaverLogin(request: NaverLoginRequest): LoginResponse {
        return basicClient.post("$baseUrl/api/v2/login/naver") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun postLogout(): HttpStatusCode {
        return apiClient.post("$baseUrl/api/v2/auth/logout") {
            contentType(ContentType.Application.Json)
        }.status
    }

    suspend fun deleteUser() {
        apiClient.delete("$baseUrl/api/v2/auth/user") {
            contentType(ContentType.Application.Json)
        }
    }
}
