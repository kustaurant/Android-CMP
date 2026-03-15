package com.kus.data.draw.api

import com.kus.data.draw.remote.DrawRestaurantResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DrawApi(
    private val client : HttpClient
) {
    suspend fun getDrawRestaurantList(
        cuisines: String,
        locations: String,
    ): List<DrawRestaurantResponse> {
        return client.get("/api/v2/draw") {
            parameter("cuisines", cuisines)
            parameter("locations", locations)
        }.body()
    }
}