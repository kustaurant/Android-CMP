package com.kus.domain.auth.usecase

import com.kus.domain.auth.repository.AuthRepository
import com.kus.domain.auth.repository.AuthTokenStore

class LogoutUseCase(
    private val tokenStore: AuthTokenStore,
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean {
        repository.postLogout()
            .onSuccess {
                tokenStore.clear()
                return true
            }
        return false
    }
}