package com.kus.shared.domain.detail.usecase

import com.kus.shared.domain.detail.repository.DetailRepository
import com.kus.shared.domain.model.detail.RestaurantReview

class GetRestaurantReviewsUseCase(
    private val detailRepository: DetailRepository,
) {
    suspend operator fun invoke(restaurantId: Long, sort: String): List<RestaurantReview> {
        return detailRepository.getRestaurantReviews(restaurantId, sort)
    }
}
