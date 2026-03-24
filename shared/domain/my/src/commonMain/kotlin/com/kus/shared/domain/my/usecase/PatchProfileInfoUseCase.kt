package com.kus.shared.domain.my.usecase

import MyRepository
import com.kus.shared.domain.model.my.ProfileInfo

class PatchProfileInfoUseCase(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(
        nickname: String?,
        phoneNumber: String?,
    ): ProfileInfo {
        return repository.patchProfileInfo(nickname, phoneNumber)
    }
}