package com.kus.shared.domain.my.usecase

import com.kus.shared.domain.model.my.MyPostItem
import com.kus.shared.domain.my.repository.MyCommunityRepository

class GetMyScrapsUseCase (
    private val repository: MyCommunityRepository,
) {
    suspend operator fun invoke(): List<MyPostItem> {
        return repository.getMyCommunityScraps()
    }
}
