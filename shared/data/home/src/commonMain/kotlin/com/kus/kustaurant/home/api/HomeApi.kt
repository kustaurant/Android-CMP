package com.kus.kustaurant.home.api

import com.kus.kustaurant.home.remote.response.HomeInfoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class HomeApi(
    private val client: HttpClient,
) {
    suspend fun getHomeInfo() : Result<HomeInfoResponse> = runCatching {
        client.get("/api/v2/home").body()
    }
}
