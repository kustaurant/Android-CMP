package com.kus.shared.data.search.api

import com.kus.data.network.ApiClientProvider
import com.kus.shared.data.search.remote.response.SearchResultResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchApi(
    private val apiClientProvider: ApiClientProvider,
) {
    private val client get() = apiClientProvider.client

    suspend fun getSearchResult(
        searchTerm: String,
        page: Int,
    ): SearchResultResponse {
        return client.get("/api/v3/search") {
            parameter("kw", searchTerm)
            parameter("page", page)
        }.body()
    }
}