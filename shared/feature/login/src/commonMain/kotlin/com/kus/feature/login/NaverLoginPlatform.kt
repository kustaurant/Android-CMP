package com.kus.feature.login

import com.kus.feature.login.model.SocialLoginResult

interface NaverLoginPlatform {
    suspend fun login(): SocialLoginResult
    suspend fun logout(): Result<Unit>
    suspend fun disconnect(): Result<Unit>
}