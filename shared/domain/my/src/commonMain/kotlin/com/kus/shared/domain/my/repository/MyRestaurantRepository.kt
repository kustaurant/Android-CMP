package com.kus.shared.domain.my.repository

import com.kus.shared.domain.model.my.EvaluatedResItem
import com.kus.shared.domain.model.my.FavoriteResItem

interface MyRestaurantRepository {
    suspend fun getFavoriteRes(): List<FavoriteResItem>
    suspend fun getEvaluatedRes(): List<EvaluatedResItem>
}
