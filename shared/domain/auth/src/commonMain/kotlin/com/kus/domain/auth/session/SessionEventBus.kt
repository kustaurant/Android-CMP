package com.kus.domain.auth.session

import kotlinx.coroutines.flow.MutableSharedFlow

class SessionEventBus : SessionEventEmitter {
    private val _events = MutableSharedFlow<SessionEvent>( extraBufferCapacity = 1 )
    val events: kotlinx.coroutines.flow.SharedFlow<SessionEvent> = _events

    override suspend fun emit(event: SessionEvent) {
        _events.tryEmit(event)
    }
}
