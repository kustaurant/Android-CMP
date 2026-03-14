package com.kus.domain.auth.usecase

import com.kus.domain.auth.repository.AuthTokenStore

class DeleteUserInfoUseCase(
    private val tokenStore: AuthTokenStore,
) {
    suspend operator fun invoke(): Boolean {
        tokenStore.clear()
        return true
    }
}
