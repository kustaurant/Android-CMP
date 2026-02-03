package com.kus.data.login.repository

import com.kus.data.login.api.AuthApi
import com.kus.data.login.remote.mapper.toDomain
import com.kus.data.login.remote.request.NaverLoginRequest
import com.kus.domain.login.model.AuthToken
import com.kus.domain.login.repository.AuthRepository

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
            Unit
        }

    override suspend fun deleteUser() {
        authApi.deleteUser()
    }
}
