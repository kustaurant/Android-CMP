package com.kus.domain.auth.usecase

import com.kus.domain.auth.model.AuthToken
import com.kus.domain.auth.repository.AuthRepository
import com.kus.domain.auth.repository.AuthTokenStore
import com.kus.domain.auth.repository.DeviceIdManager

class PostNaverLoginUseCase(
    private val authRepository: AuthRepository,
    private val tokenStore: AuthTokenStore,
    private val deviceIdManager: DeviceIdManager,
) {
    suspend operator fun invoke(providerId: String, naverAccessToken: String): AuthToken {
        val token = authRepository.postNaverLogin(
            provider = "NAVER",
            providerId = providerId,
            naverAccessToken = naverAccessToken
        )

        tokenStore.save(token)
        deviceIdManager.clear()
        return token
    }
}
