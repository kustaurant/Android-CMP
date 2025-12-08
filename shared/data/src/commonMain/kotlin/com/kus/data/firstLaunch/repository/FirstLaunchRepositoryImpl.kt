package com.kus.data.firstLaunch.repository

import com.kus.data.firstLaunch.dataSource.FirstLaunchLocalDataSource
import com.kus.domain.firstLaunch.repository.FirstLaunchRepository
import kotlinx.coroutines.flow.Flow

class FirstLaunchRepositoryImpl(
    private val local: FirstLaunchLocalDataSource
) : FirstLaunchRepository {
    override val isFirstLaunchFlow: Flow<Boolean> = local.isFirstLaunchFlow
    override suspend fun setOnboardingSeen() = local.setOnboardingSeen()
}