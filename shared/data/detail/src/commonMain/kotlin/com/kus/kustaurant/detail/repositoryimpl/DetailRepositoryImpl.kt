package com.kus.kustaurant.detail.repositoryimpl

import com.kus.kustaurant.detail.api.DetailApi
import com.kus.kustaurant.detail.remote.mapper.toDomain
import com.kus.shared.domain.detail.repository.DetailRepository
import com.kus.shared.domain.model.detail.RestaurantDetail

class DetailRepositoryImpl(
    private val api: DetailApi,
) : DetailRepository {
    override suspend fun getRestaurantDetail(restaurantId: Long): RestaurantDetail =
        api.getRestaurantDetail(restaurantId).toDomain()
}
