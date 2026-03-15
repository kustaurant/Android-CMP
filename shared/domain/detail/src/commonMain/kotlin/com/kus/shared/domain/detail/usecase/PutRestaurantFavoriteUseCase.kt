package com.kus.shared.domain.detail.usecase

import com.kus.shared.domain.detail.repository.DetailRepository
import com.kus.shared.domain.model.detail.FavoriteResult

class PutRestaurantFavoriteUseCase(
    private val detailRepository: DetailRepository,
) {
    suspend operator fun invoke(restaurantId: Long): FavoriteResult {
        return detailRepository.putRestaurantFavorite(restaurantId)
    }
}
