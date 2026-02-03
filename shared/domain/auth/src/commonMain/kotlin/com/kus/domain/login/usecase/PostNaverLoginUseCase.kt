package com.kus.domain.login.usecase

import com.kus.domain.login.model.AuthToken
import com.kus.domain.login.repository.AuthRepository

class PostNaverLoginUseCase(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(provider: String, providerId: String, naverAccessToken: String): AuthToken {
        return authRepository.postNaverLogin(provider, providerId, naverAccessToken)
    }
}