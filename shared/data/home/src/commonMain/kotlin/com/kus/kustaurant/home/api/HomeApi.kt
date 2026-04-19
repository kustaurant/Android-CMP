package com.kus.kustaurant.home.api

import com.kus.data.network.ApiClientProvider
import com.kus.kustaurant.home.remote.response.HomeInfoResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

class HomeApi(
    private val apiClientProvider: ApiClientProvider,
) {
    private val client get() = apiClientProvider.client

    suspend fun getHomeInfo(): HomeInfoResponse {
        return client.get("/api/v2/home").body()
    }
}
