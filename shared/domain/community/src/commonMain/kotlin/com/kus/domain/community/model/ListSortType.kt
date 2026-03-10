package com.kus.domain.community.model

enum class ListSortType(val value: String) {
    POPULARITY("POPULARITY"),
    LATEST("LATEST");
}

fun ListSortType.toCategorySort(): ListSortType =
    when (this) {
        ListSortType.LATEST -> ListSortType.LATEST
        ListSortType.POPULARITY -> ListSortType.POPULARITY
    }