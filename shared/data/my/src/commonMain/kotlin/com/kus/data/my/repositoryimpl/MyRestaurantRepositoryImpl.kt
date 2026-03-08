package com.kus.data.my.repositoryimpl

import com.kus.data.my.api.MyRestaurantApi
import com.kus.data.my.mapper.toDomain
import com.kus.shared.domain.model.my.EvaluatedResItem
import com.kus.shared.domain.model.my.FavoriteResItem
import com.kus.shared.domain.my.repository.MyRestaurantRepository

class MyRestaurantRepositoryImpl(
    private val api: MyRestaurantApi,
) : MyRestaurantRepository {

    override suspend fun getFavoriteRes(): List<FavoriteResItem> =
        api.getFavoriteRes().map { it.toDomain() }

    override suspend fun getEvaluatedRes(): List<EvaluatedResItem> =
        api.getEvaluatedRes().map { it.toDomain() }
}
