package com.kus.shared.data.tier.repository

import com.kus.shared.data.tier.api.TierApi
import com.kus.shared.data.tier.mapper.toCuisineQuery
import com.kus.shared.data.tier.mapper.toDomain
import com.kus.shared.data.tier.mapper.toLocationQuery
import com.kus.shared.data.tier.mapper.toSituationQuery
import com.kus.shared.domain.model.tier.TierMapData
import com.kus.shared.domain.model.tier.TierRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation
import com.kus.shared.domain.tier.repository.TierRepository

class TierRepositoryImpl(
    private val api: TierApi
) : TierRepository {

    override suspend fun getRestaurantList(
        cuisines: Set<Cuisine>,
        situations: Set<Situation>,
        locations: Set<Location>,
        page: Int
    ): List<TierRestaurant> {
        return api.getRestaurantList(
            cuisines = cuisines.toCuisineQuery(),
            situations = situations.toSituationQuery(),
            locations = locations.toLocationQuery(),
            page = page
        ).restaurants.map { it.toDomain() }
    }

    override suspend fun getRestaurantMapList(
        cuisines: Set<Cuisine>,
        situations: Set<Situation>,
        locations: Set<Location>,
    ): TierMapData {
        return api.getTierMapList(
            cuisines = cuisines.toCuisineQuery(),
            situations = situations.toSituationQuery(),
            locations = locations.toLocationQuery(),
        ).toDomain()
    }
}