package com.kus.shared.data.search.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ResultItemResponse(
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
    val titleHighlights: List<Highlights>,
    val categoryHighlights: List<Highlights>,
    val matchedFields: List<String>,
)

@Serializable
data class Highlights(
    val start: Int,
    val end: Int,
)
