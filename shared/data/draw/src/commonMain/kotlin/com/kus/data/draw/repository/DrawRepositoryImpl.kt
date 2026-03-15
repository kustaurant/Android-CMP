package com.kus.data.draw.repository

import com.kus.data.draw.api.DrawApi
import com.kus.data.draw.mapper.DrawQueryMapper.toCuisineQuery
import com.kus.data.draw.mapper.DrawQueryMapper.toLocationQuery
import com.kus.data.mapper.toDomain
import com.kus.domain.draw.model.DrawRestaurant
import com.kus.domain.draw.repository.DrawRepository
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location

class DrawRepositoryImpl(
    private val drawApi: DrawApi
) : DrawRepository {

    override suspend fun getDrawRestaurantData(
        cuisines: Set<Cuisine>,
        locations: Set<Location>
    ): List<DrawRestaurant> {

        return drawApi.getDrawRestaurantList(
            cuisines = cuisines.toCuisineQuery(),
            locations = locations.toLocationQuery()
        ).map { it.toDomain() }
    }
}