package com.kus.shared.domain.my.usecase

import MyRepository
import com.kus.shared.domain.model.my.MyInfo

class GetMyInfoUseCase (
    private val repository: MyRepository,
) {
    suspend operator fun invoke(): MyInfo {
        return repository.getMyInfo()
    }
}
