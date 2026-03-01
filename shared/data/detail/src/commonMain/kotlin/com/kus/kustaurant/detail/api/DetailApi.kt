package com.kus.kustaurant.detail.api

import com.kus.kustaurant.detail.remote.response.DetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class DetailApi(
    private val client: HttpClient,
) {
    suspend fun getRestaurantDetail(restaurantId: Long): DetailResponse {
        return client.get("/api/v2/restaurants/$restaurantId").body()
    }
}
