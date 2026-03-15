package com.kus.domain.draw.usecase

import com.kus.domain.draw.model.DrawRestaurant
import com.kus.domain.draw.repository.DrawRepository
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location

class GetDrawRestaurantUseCase(
    private val drawRepository: DrawRepository
) {
    suspend operator fun invoke(cuisines: Set<Cuisine>, locations: Set<Location>):  List<DrawRestaurant> {
        return drawRepository.getDrawRestaurantData(cuisines, locations)
    }
}