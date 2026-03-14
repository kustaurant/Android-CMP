package com.kus.shared.domain.search.usecase

import com.kus.shared.domain.model.search.SearchResult
import com.kus.shared.domain.search.repository.SearchRepository

class GetSearchResultUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(searchTerm: String, page: Int): SearchResult =
        searchRepository.getSearchResult(searchTerm, page)
}