package com.kus.shared.domain.model.search

data class SearchResult(
    val items: List<ResultItem>,
    val hasNext: Boolean,
)
