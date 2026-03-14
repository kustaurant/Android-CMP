package com.kus.shared.data.search.api

import com.kus.shared.data.search.remote.response.SearchResultResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchApi(
    private val client: HttpClient,
) {
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