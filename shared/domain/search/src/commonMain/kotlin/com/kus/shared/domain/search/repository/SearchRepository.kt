package com.kus.shared.domain.search.repository

import com.kus.shared.domain.model.restaurant.RestaurantItem

interface SearchRepository {
    suspend fun getSearchResult(
        searchTerm: String,
    ): List<RestaurantItem>
}