package com.kus.shared.domain.search.usecase

import com.kus.shared.domain.model.restaurant.RestaurantItem
import com.kus.shared.domain.search.repository.SearchRepository

class GetSearchResultUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(searchTerm: String): List<RestaurantItem> =
        searchRepository.getSearchResult(searchTerm)
}