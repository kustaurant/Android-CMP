package com.kus.shared.domain.detail.repository

import com.kus.shared.domain.model.detail.RestaurantDetail

interface DetailRepository {
    suspend fun getRestaurantDetail(restaurantId: Long): RestaurantDetail
}
