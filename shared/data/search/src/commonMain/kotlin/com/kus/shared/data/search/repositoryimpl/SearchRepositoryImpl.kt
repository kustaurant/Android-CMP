package com.kus.shared.data.search.repositoryimpl

import com.kus.shared.data.search.api.SearchApi
import com.kus.shared.data.search.mapper.toDomain
import com.kus.shared.domain.model.search.SearchResult
import com.kus.shared.domain.search.repository.SearchRepository

class SearchRepositoryImpl(
    private val api: SearchApi,
): SearchRepository {
    override suspend fun getSearchResult(searchTerm: String): SearchResult {
        return api.getSearchResult(searchTerm).toDomain()
    }

}
