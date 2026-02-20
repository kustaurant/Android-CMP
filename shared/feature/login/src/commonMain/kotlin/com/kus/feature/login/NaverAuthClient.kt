package com.kus.feature.login

import com.kus.feature.login.model.NaverAuthResult

interface SocialAuthClient {
    suspend fun login(): NaverAuthResult
}