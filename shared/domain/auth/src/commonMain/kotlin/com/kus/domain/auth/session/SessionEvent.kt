package com.kus.domain.auth.session

sealed interface SessionEvent {
    data object Expired : SessionEvent
    data object LoginRequired : SessionEvent
}