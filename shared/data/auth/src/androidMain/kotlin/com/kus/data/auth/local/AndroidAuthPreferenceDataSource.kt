package com.kus.data.auth.local

import android.content.Context
import androidx.core.content.edit

class AndroidAuthPreferenceDataSource(
    context: Context
) : AuthPreferenceDataSource {

    private val prefs = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    override suspend fun setUserId(value: String) {
        prefs.edit { putString(KEY_USER_ID, value) }
    }

    override suspend fun setAccessToken(value: String) {
        prefs.edit { putString(KEY_ACCESS_TOKEN, value) }
    }

    override suspend fun setRefreshToken(value: String) {
        prefs.edit { putString(KEY_REFRESH_TOKEN, value) }
    }

    override suspend fun getUserId(): String {
        return prefs.getString(KEY_USER_ID, "") ?: ""
    }

    override suspend fun getAccessToken(): String {
        return prefs.getString(KEY_ACCESS_TOKEN, "") ?: ""
    }

    override suspend fun getRefreshToken(): String {
        return prefs.getString(KEY_REFRESH_TOKEN, "") ?: ""
    }

    override suspend fun clear() {
        prefs.edit { clear() }
    }

    companion object {
        private const val PREF_NAME = "auth_prefs"

        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_ACCESS_TOKEN = "key_access_token"
        private const val KEY_REFRESH_TOKEN = "key_refresh_token"
    }
}
