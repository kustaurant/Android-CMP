package com.kus.kustaurant.home.repositoryimpl

import com.kus.kustaurant.home.api.HomeApi
import com.kus.kustaurant.home.remote.mapper.toDomain
import com.kus.shared.domain.home.repository.HomeRepository
import com.kus.shared.domain.model.home.HomeInfo

class HomeRepositoryImpl(
    private val api: HomeApi,
) : HomeRepository {
    override suspend fun getHomeInfo(): HomeInfo =
        api.getHomeInfo().toDomain()

}
