package com.kus.data.my.api

import com.kus.data.my.remote.response.EvaluatedResponse
import com.kus.data.my.remote.response.FavoriteResponse
import com.kus.data.network.ApiClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

class MyRestaurantApi(
    private val apiClientProvider: ApiClientProvider,
) {
    private val client get() = apiClientProvider.client

    suspend fun getFavoriteRes(): List<FavoriteResponse> {
        return client.get("/api/v2/auth/mypage/restaurants/favorite").body()
    }

    suspend fun getEvaluatedRes(): List<EvaluatedResponse> {
        return client.get("/api/v2/auth/mypage/restaurants/evaluated").body()
    }
}
