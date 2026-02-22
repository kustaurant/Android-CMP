package com.kus.shared.domain.model.tier.filter


enum class Situation {
    ALL,
    SOLO,
    TWO_TO_FOUR,
    FIVE_OR_MORE,
    COMPANY_DINNER,
    DELIVERY,
    LATE_NIGHT,
    INVITE_FRIENDS,
    DATE,
    BLIND_DATE;

    fun toLabel(): String = when (this) {
        ALL -> "전체"
        SOLO -> "혼밥"
        TWO_TO_FOUR -> "2~4인"
        FIVE_OR_MORE -> "5인 이상"
        COMPANY_DINNER -> "단체 회식"
        DELIVERY -> "배달"
        LATE_NIGHT -> "야식"
        INVITE_FRIENDS -> "친구 초대"
        DATE -> "데이트"
        BLIND_DATE -> "소개팅"
    }

    companion object {
        fun normalize(values: Set<Situation>): Set<Situation> {
            return when {
                values.contains(ALL) -> setOf(ALL)
                values.isEmpty() -> setOf(ALL)
                else -> values
            }
        }
    }
}