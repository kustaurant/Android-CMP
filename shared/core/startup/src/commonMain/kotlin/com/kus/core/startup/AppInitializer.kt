package com.kus.core.startup

import com.kus.data.network.ApiClientProvider
import com.kus.domain.auth.session.SessionEvent
import com.kus.domain.auth.session.SessionEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppInitializer(
    private val sessionBus: SessionEventBus,
    private val apiClientProvider: ApiClientProvider,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun initialize() {
        scope.launch {
            sessionBus.events.collect { event ->
                when (event) {
                    SessionEvent.LoggedOut -> {
                        apiClientProvider.reset()
                    }
                    else -> {}
                }
            }
        }
    }
}