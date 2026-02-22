package com.kus.shared.domain.tier.usecase

import com.kus.shared.domain.model.tier.TierRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation
import com.kus.shared.domain.tier.repository.TierRepository

class GetTierRestaurantListUseCase(
    private val tierRepository: TierRepository
) {
    suspend operator fun invoke(
        cuisines: Set<Cuisine>,
        situations: Set<Situation>,
        locations: Set<Location>,
        page: Int,
    ): List<TierRestaurant> {
        return tierRepository.getRestaurantList(cuisines, situations, locations, page)
    }
}