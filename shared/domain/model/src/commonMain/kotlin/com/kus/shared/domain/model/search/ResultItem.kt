package com.kus.shared.domain.model.search

data class ResultItem(
    val id: Long,
    val name: String,
    val cuisine: String,
    val position: String,
    val imgUrl: String,
    val tier: Int,
    val partnershipInfo: String? = null,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val isTempTier: Boolean? = null,
    val matchedMenus: List<String>,
    val titleHighlights: List<HighlightsItem>,
    val categoryHighlights: List<HighlightsItem>,
    val matchedFields: List<String>,
)

data class HighlightsItem(
    val start: Int,
    val end: Int,
)
