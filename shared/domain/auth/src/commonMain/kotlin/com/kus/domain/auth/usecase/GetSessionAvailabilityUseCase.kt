package com.kus.domain.auth.usecase

import com.kus.domain.auth.repository.AuthTokenStore

class GetSessionAvailabilityUseCase(
    private val store: AuthTokenStore,
) {
    suspend operator fun invoke() : Boolean {
        return store.getAccessTokenOrBlank().isNotEmpty() && store.getRefreshTokenOrBlank().isNotEmpty()
    }
}