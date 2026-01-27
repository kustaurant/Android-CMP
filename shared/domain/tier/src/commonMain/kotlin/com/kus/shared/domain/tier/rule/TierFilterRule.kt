package com.kus.shared.domain.tier.rule

object TierFilterRule {
    /**
     * "전체" 규칙:
     * - 전체 클릭: 전체만 남김
     * - 다른거 클릭: 전체 해제 + 토글
     * - 아무것도 안남으면 전체로 복귀
     */
    fun <T> toggleWithAll(
        current: Set<T>,
        clicked: T,
        all: T,
    ): Set<T> {
        if (clicked == all) return setOf(all)

        val next = current.toMutableSet()

        next.remove(all)

        if (next.contains(clicked)) next.remove(clicked) else next.add(clicked)

        return if (next.isEmpty()) setOf(all) else next
    }

    /**
     * "전체" + "특정 항목은 단독(exclusive)" 규칙(=음식의 제휴업체)
     * - 클릭한게 exclusive면: 그거만 남김
     * - 클릭한게 전체면: 전체만 남김
     * - 나머지는 toggleWithAll과 동일
     */
    fun <T> toggleWithAllAndExclusive(
        current: Set<T>,
        clicked: T,
        all: T,
        exclusive: Set<T>,
    ): Set<T> {
        if (clicked in exclusive) return setOf(clicked)

        if (clicked == all) return setOf(all)

        val next = current.toMutableSet()

        next.remove(all)
        next.removeAll(exclusive)

        if (next.contains(clicked)) next.remove(clicked) else next.add(clicked)

        return if (next.isEmpty()) setOf(all) else next
    }
}