package com.kus.domain.auth.usecase

import com.kus.domain.auth.repository.AuthRepository
import com.kus.domain.auth.repository.AuthTokenStore
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventEmitter

class LogoutUseCase(
    private val tokenStore: AuthTokenStore,
    private val repository: AuthRepository,
    private val sessionEvents: SessionEventEmitter,
) {
    suspend operator fun invoke(): Boolean {
        repository.postLogout()
            .onSuccess {
                tokenStore.clear()
                sessionEvents.emit(SessionEvent.LoggedOut)
                return true
            }
        return false
    }
}