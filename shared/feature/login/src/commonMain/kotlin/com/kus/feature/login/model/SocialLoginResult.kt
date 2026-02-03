package com.kus.feature.login.model

sealed class SocialLoginResult {
    data class Success(val accessToken: String) : SocialLoginResult()
    data class Cancelled(val reason: String? = null) : SocialLoginResult()
    data class Failure(val code: String, val message: String?) : SocialLoginResult()
}