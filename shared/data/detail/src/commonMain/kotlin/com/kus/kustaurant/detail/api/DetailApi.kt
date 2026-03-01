package com.kus.kustaurant.detail.api

import com.kus.kustaurant.detail.remote.response.DetailResponse
import com.kus.kustaurant.detail.remote.response.ReviewResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DetailApi(
    private val client: HttpClient,
) {
    suspend fun getRestaurantDetail(restaurantId: Long): DetailResponse {
        return client.get("/api/v2/restaurants/$restaurantId").body()
    }

    suspend fun getRestaurantReviews(restaurantId: Long, sort: String = "POPULARITY"): List<ReviewResponse> {
        return client.get("/api/v2/restaurants/$restaurantId/comments") {
            parameter("sort", sort)
        }.body()
    }
}
