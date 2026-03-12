package com.kus.domain.auth.repository

interface DeviceIdManager {
    suspend fun getOrCreateDeviceId(): String
    suspend fun saveDeviceId(id: String)
    suspend fun clear()
}