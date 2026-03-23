package com.kus.shared.domain.my.usecase

import MyRepository

class PatchProfileInfoUseCase(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(
        nickname: String,
        phoneNumber: String?,
    ): String {
        return repository.patchProfileInfo(nickname, phoneNumber)
    }
}