package com.kus.shared.domain.model.tier.filter

enum class Cuisine {
    ALL,
    KOREAN,
    JAPANESE,
    CHINESE,
    WESTERN,
    ASIAN,
    MEAT,
    CHICKEN,
    SEAFOOD,
    BURGER_PIZZA,
    BUNSIK,
    PUB,
    CAFE_DESSERT,
    BAKERY,
    SALAD,
    PARTNERSHIP;

    fun toLabel(): String = when (this) {
        ALL -> "전체"
        KOREAN -> "한식"
        JAPANESE -> "일식"
        CHINESE -> "중식"
        WESTERN -> "양식"
        ASIAN -> "아시안"
        MEAT -> "고기"
        CHICKEN -> "치킨"
        SEAFOOD -> "해산물"
        BURGER_PIZZA -> "햄버거/피자"
        BUNSIK -> "분식"
        PUB -> "술집"
        CAFE_DESSERT -> "카페/디저트"
        BAKERY -> "베이커리"
        SALAD -> "샐러드"
        PARTNERSHIP -> "제휴업체"
    }

    companion object {
        fun normalize(values: Set<Cuisine>): Set<Cuisine> {
            return when {
                values.contains(ALL) -> setOf(ALL)
                values.contains(PARTNERSHIP) -> setOf(PARTNERSHIP)
                values.isEmpty() -> setOf(ALL)
                else -> values
            }
        }
    }
}