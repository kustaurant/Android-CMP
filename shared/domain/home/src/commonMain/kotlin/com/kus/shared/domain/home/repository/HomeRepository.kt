package com.kus.shared.domain.home.repository

import com.kus.shared.domain.model.home.HomeInfo

interface HomeRepository {
    suspend fun getHomeInfo(): HomeInfo
}
