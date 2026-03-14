package com.kus.shared.data.search.mapper

import com.kus.shared.data.search.remote.response.Highlights
import com.kus.shared.data.search.remote.response.ResultItemResponse
import com.kus.shared.data.search.remote.response.SearchResultResponse
import com.kus.shared.domain.model.search.HighlightsItem
import com.kus.shared.domain.model.search.ResultItem
import com.kus.shared.domain.model.search.SearchResult

fun SearchResultResponse.toDomain(): SearchResult {
    return SearchResult(
        items = items.map { it.toDomain() },
        hasNext = hasNext,
    )
}

fun ResultItemResponse.toDomain(): ResultItem {
    return ResultItem(
        id = id,
        name = name,
        cuisine = cuisine,
        position = position,
        imgUrl = imgUrl,
        tier = tier,
        partnershipInfo = partnershipInfo,
        isEvaluated = isEvaluated,
        isFavorite = isFavorite,
        isTempTier = isTempTier,
        matchedMenus = matchedMenus,
        titleHighlights = titleHighlights.map { it.toDomain() },
        categoryHighlights = categoryHighlights.map { it.toDomain() },
        matchedFields = matchedFields,
    )
}

fun Highlights.toDomain(): HighlightsItem {
    return HighlightsItem(start = this.start, end = this.end,)
}
