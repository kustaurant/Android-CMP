package com.kus.data.auth

import com.kus.domain.auth.repository.DeviceIdManager
import com.russhwolf.settings.Settings

class DeviceIdManagerImpl(private val settings: Settings) : DeviceIdManager {

    override suspend fun getOrCreateDeviceId(): String {
        return settings.getStringOrNull(KEY_DEVICE_ID) ?: run {
            val newId = randomUUID()
            settings.putString(KEY_DEVICE_ID, newId)
            newId
        }
    }

    override suspend fun saveDeviceId(id: String) {
        settings.putString(KEY_DEVICE_ID, id)
    }

    override suspend fun clear() {
        settings.remove(KEY_DEVICE_ID)
    }

    companion object {
        private const val KEY_DEVICE_ID = "device_id"
    }
}