package com.kus.data.my.repositoryimpl

import MyRepository
import com.kus.data.my.api.MyApi
import com.kus.data.my.mapper.toDomain
import com.kus.data.my.remote.response.request.FeedbackRequest
import com.kus.data.my.remote.response.request.PatchProfileInfoRequest
import com.kus.shared.domain.model.my.MyInfo
import com.kus.shared.domain.model.my.ProfileInfo

class MyRepositoryImpl(
    private val api: MyApi,
) : MyRepository {
    override suspend fun getMyInfo(): MyInfo =
        api.getMyInfo().toDomain()

    override suspend fun postFeedback(content: String): String =
        api.postFeedback(FeedbackRequest(content))

    override suspend fun patchProfileInfo(
        nickname: String?,
        phoneNumber: String?,
    ): ProfileInfo =
        api.patchProfileInfo(PatchProfileInfoRequest(nickname, phoneNumber)).toDomain()
}
