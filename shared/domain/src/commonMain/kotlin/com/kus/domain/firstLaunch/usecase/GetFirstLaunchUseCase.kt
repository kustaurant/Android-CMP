package com.kus.domain.firstLaunch.usecase

import com.kus.domain.firstLaunch.repository.FirstLaunchRepository
import kotlinx.coroutines.flow.Flow

class GetFirstLaunchUseCase (
    private val repo: FirstLaunchRepository
    ) {
        operator fun invoke(): Flow<Boolean> = repo.isFirstLaunchFlow
    }