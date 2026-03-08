package com.kus.data.network.model

sealed class RefreshResult {
    data class Success(val accessToken: String) : RefreshResult()
    object InvalidRefreshToken : RefreshResult()
    object NetworkError : RefreshResult()
}