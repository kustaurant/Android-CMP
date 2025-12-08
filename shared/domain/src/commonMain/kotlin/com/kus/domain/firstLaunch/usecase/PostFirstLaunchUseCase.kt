package com.kus.domain.firstLaunch.usecase

import com.kus.domain.firstLaunch.repository.FirstLaunchRepository


class PostFirstLaunchUseCase (
    private val repo: FirstLaunchRepository
) {
    suspend operator fun invoke() = repo.setOnboardingSeen()
}