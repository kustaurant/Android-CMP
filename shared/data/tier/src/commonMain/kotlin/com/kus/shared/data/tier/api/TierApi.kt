package com.kus.shared.data.tier.api

import com.kus.shared.data.tier.remote.response.TierListResponse
import com.kus.shared.data.tier.remote.response.TierMapDataResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TierApi(
    private val client: HttpClient,
) {
    suspend fun getRestaurantList(
        cuisines: String,
        situations: String,
        locations: String,
        page: Int,
        isAiTier : Boolean,
        limit: Int = 30,
    ): TierListResponse {
        return client.get("/api/v2/tier") {
            parameter("cuisines", cuisines)
            parameter("situations", situations)
            parameter("locations", locations)
            parameter("page", page)
            parameter("ai", isAiTier)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getTierMapList(
        cuisines: String,
        situations: String,
        locations: String,
    ): TierMapDataResponse {
        return client.get("/api/v2/tier/map") {
            parameter("cuisines", cuisines)
            parameter("situations", situations)
            parameter("locations", locations)
        }.body()
    }
}
