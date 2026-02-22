package com.kus.shared.domain.tier.usecase

import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation
import com.kus.shared.domain.tier.repository.TierRepository

class GetTierRestaurantMapUseCase(
    private val tierRepository: TierRepository
) {
    suspend operator fun invoke(
        cuisines: Set<Cuisine>,
        situations: Set<Situation>,
        locations: Set<Location>,
    ): TierMapData {
        return tierRepository.getRestaurantMapList(cuisines, situations, locations)
    }
}