package com.kus.data.auth.repository

import com.kus.data.auth.api.AuthApi
import com.kus.data.auth.remote.mapper.toDomain
import com.kus.data.auth.remote.request.NaverLoginRequest
import com.kus.domain.auth.model.AuthToken
import com.kus.domain.auth.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
) : AuthRepository {

    override suspend fun postNaverLogin(
        provider: String,
        providerId: String,
        naverAccessToken: String
    ): AuthToken {
        val request = NaverLoginRequest(
            provider = provider,
            providerId = providerId,
            token = naverAccessToken,
            authCode = null
        )
        return authApi.postNaverLogin(request).toDomain()
    }

    override suspend fun postLogout(): Result<Unit> =
        runCatching {
            authApi.postLogout()
        }

    override suspend fun deleteUser() {
        authApi.deleteUser()
    }
}
