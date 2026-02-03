package com.kus.data.login.api

import com.kus.data.login.remote.request.NaverLoginRequest
import com.kus.data.login.remote.response.LoginResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthApi(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) {
    suspend fun postNaverLogin(request: NaverLoginRequest): LoginResponse {
        return httpClient.post("$baseUrl/api/v2/login/naver") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun postLogout(): HttpStatusCode {
        return httpClient.post("$baseUrl/api/v2/auth/logout") {
            contentType(ContentType.Application.Json)
        }.status
    }

    suspend fun deleteUser() {
        httpClient.delete("$baseUrl/api/v2/auth/user") {
            contentType(ContentType.Application.Json)
        }
    }
}
