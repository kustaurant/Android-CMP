package com.kus.feature.login.model

sealed interface NaverAuthResult {
    data class Success(val payload: NaverAuthPayload) : NaverAuthResult
    data class Failure(val code: String, val message: String?) : NaverAuthResult
    data class Cancelled(val reason: String? = null) : NaverAuthResult
}
