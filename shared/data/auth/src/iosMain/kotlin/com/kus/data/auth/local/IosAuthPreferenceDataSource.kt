package com.kus.data.auth.local

import platform.Foundation.NSUserDefaults

class IosAuthPreferenceDataSource(
    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()
) : AuthPreferenceDataSource {

    private object Keys {
        const val USER_ID = "auth.user_id"
        const val ACCESS_TOKEN = "auth.access_token"
        const val REFRESH_TOKEN = "auth.refresh_token"
    }

    override suspend fun setUserId(value: String) {
        defaults.setObject(value, forKey = Keys.USER_ID)
    }

    override suspend fun setAccessToken(value: String) {
        defaults.setObject(value, forKey = Keys.ACCESS_TOKEN)
    }

    override suspend fun setRefreshToken(value: String) {
        defaults.setObject(value, forKey = Keys.REFRESH_TOKEN)
    }

    override suspend fun getUserId(): String {
        return defaults.stringForKey(Keys.USER_ID) ?: ""
    }

    override suspend fun getAccessToken(): String {
        return defaults.stringForKey(Keys.ACCESS_TOKEN) ?: ""
    }

    override suspend fun getRefreshToken(): String {
        return defaults.stringForKey(Keys.REFRESH_TOKEN) ?: ""
    }

    override suspend fun clear() {
        defaults.removeObjectForKey(Keys.USER_ID)
        defaults.removeObjectForKey(Keys.ACCESS_TOKEN)
        defaults.removeObjectForKey(Keys.REFRESH_TOKEN)
    }
}
