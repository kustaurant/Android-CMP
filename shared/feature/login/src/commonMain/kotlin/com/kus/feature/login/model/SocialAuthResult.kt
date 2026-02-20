package com.kus.feature.login.model

sealed class SocialAuthResult {
    data class Success(val accessToken: String) : SocialAuthResult()
    data class Cancelled(val reason: String? = null) : SocialAuthResult()
    data class Failure(val code: String, val message: String?) : SocialAuthResult()
}