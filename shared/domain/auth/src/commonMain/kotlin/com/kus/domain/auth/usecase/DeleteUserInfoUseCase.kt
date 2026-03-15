package com.kus.domain.auth.usecase

import com.kus.domain.auth.repository.AuthRepository
import com.kus.domain.auth.repository.AuthTokenStore

class DeleteUserInfoUseCase(
    private val tokenStore: AuthTokenStore,
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean {
        repository.deleteUser()
        tokenStore.clear()
        return true
    }
}
