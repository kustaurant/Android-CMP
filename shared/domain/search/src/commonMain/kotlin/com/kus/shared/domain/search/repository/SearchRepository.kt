package com.kus.shared.domain.search.repository

import com.kus.shared.domain.model.search.SearchResult

interface SearchRepository {
    suspend fun getSearchResult(
        searchTerm: String,
    ): SearchResult
}
