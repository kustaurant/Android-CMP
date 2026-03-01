package com.kus.domain.community.model

enum class RankingSortType(val value : String) {
    SEASONAL("SEASONAL"),
    CUMULATIVE("CUMULATIVE")
}

fun String.toRankingSort(): RankingSortType {
    return when (this) {
        "seasonal" -> RankingSortType.SEASONAL
        "cumulative" -> RankingSortType.CUMULATIVE
        else -> RankingSortType.SEASONAL
    }
}