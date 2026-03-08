package com.kus.data.my.repositoryimpl

import MyRepository
import com.kus.data.my.api.MyApi
import com.kus.data.my.mapper.toDomain
import com.kus.shared.domain.model.my.MyInfo

class MyRepositoryImpl(
    private val api: MyApi,
) : MyRepository {
    override suspend fun getMyInfo(): MyInfo =
        api.getMyInfo().toDomain()

    override suspend fun postFeedback(content: String): String =
        api.postFeedback(content)

}
