package com.kus.domain.auth.session

interface SessionEventEmitter {
    suspend fun emit(event: SessionEvent)
}