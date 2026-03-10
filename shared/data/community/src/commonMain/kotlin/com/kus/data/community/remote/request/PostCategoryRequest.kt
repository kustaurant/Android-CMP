package com.kus.data.community.remote.request

import kotlinx.serialization.SerialName
import com.kus.domain.community.model.PostCategory

enum class PostCategoryRequest{
    @SerialName("전체게시판")
    ALL,
    @SerialName("자유게시판")
    FREE,
    @SerialName("칼럼게시판")
    COLUMN,
    @SerialName("건의게시판")
    SUGGESTION;

    fun toDomain(): PostCategory = when (this) {
        ALL -> PostCategory.ALL
        FREE -> PostCategory.FREE
        COLUMN -> PostCategory.COLUMN
        SUGGESTION -> PostCategory.SUGGESTION
    }
}