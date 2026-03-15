package com.kus.domain.draw.repository

import com.kus.domain.draw.model.DrawRestaurant
import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location

interface DrawRepository {
    suspend fun getDrawRestaurantData(cuisines: Set<Cuisine>, locations: Set<Location>):  List<DrawRestaurant>
}