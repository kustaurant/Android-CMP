package com.kus.domain.community.usecase

import com.kus.domain.community.model.CommunityRanking
import com.kus.domain.community.model.RankingSortType
import com.kus.domain.community.repository.CommunityRepository


class GetCommunityRankingListUseCase(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(sort: RankingSortType): List<CommunityRanking> {
        return communityRepository.getRankingList(sort)
    }
}