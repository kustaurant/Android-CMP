package com.kus.domain.login.repository

import com.kus.domain.login.model.AuthToken

interface AuthRepository {
    suspend fun postNaverLogin(provider: String, providerId: String, naverAccessToken: String): AuthToken

    suspend fun postLogout(): Result<Unit>

    suspend fun deleteUser()
}