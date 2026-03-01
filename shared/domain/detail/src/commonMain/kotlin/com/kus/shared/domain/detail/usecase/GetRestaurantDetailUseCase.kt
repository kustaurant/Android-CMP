package com.kus.shared.domain.detail.usecase

import com.kus.shared.domain.detail.repository.DetailRepository
import com.kus.shared.domain.model.detail.RestaurantDetail

class GetRestaurantDetailUseCase(
    private val detailRepository: DetailRepository,
) {
    suspend operator fun invoke(restaurantId: Long): RestaurantDetail {
        return detailRepository.getRestaurantDetail(restaurantId)
    }
}
