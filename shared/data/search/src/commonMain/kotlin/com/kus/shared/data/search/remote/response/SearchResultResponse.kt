package com.kus.shared.data.search.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultResponse(
    val items: List<ResultItemResponse>,
    val hasNext: Boolean,
)
