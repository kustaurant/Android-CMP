package com.kus.data.firstLaunch.dataSource

import kotlinx.coroutines.flow.Flow

interface FirstLaunchLocalDataSource {
    val isFirstLaunchFlow: Flow<Boolean>
    suspend fun setOnboardingSeen()
}