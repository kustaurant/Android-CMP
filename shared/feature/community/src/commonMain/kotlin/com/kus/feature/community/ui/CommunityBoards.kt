package com.kus.feature.community.ui

import com.kus.domain.community.model.PostCategory

internal object CommunityBoards {
    val list: List<PostCategory> = listOf(
        PostCategory.ALL,
        PostCategory.FREE,
        PostCategory.COLUMN,
        PostCategory.SUGGESTION
    )
}