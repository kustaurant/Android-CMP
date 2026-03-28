package com.kus.shared.domain.tier.repository

import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation

interface TierRepository {
    suspend fun getRestaurantList(
        cuisines: Set<Cuisine>,
        situations: Set<Situation>,
        locations: Set<Location>,
        page: Int,
        isAiTier : Boolean,
    ): List<TierRestaurant>

    suspend fun getRestaurantMapList(
        cuisines: Set<Cuisine>,
        situations: Set<Situation>,
        locations: Set<Location>,
    ): TierMapData
}