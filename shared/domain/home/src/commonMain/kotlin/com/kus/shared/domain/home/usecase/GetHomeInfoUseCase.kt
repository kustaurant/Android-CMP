package com.kus.shared.domain.home.usecase

import com.kus.shared.domain.home.repository.HomeRepository
import com.kus.shared.domain.model.home.HomeInfo

class GetHomeInfoUseCase(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(): HomeInfo {
        return homeRepository.getHomeInfo()
    }
}
