package com.kus.data.community.remote.response

import kotlinx.serialization.Serializable

@Serializable
class CommunityRankingResponse(
    val userId : Long,
    val nickname : String,
    val iconUrl : String,
    val evaluationCount: Int,
    val rank: Int,
)