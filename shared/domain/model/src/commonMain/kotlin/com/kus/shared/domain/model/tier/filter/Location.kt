package com.kus.shared.domain.model.tier.filter

enum class Location {
    ALL,
    KONKUK_TO_JUNGMUN,   // 건입~중문
    JUNGMUN_TO_EODAE,    // 중문~어대
    BACK_GATE,           // 후문
    FRONT_GATE,          // 정문
    GUUI_STATION ;        // 구의역


    fun toLabel(): String = when (this) {
        ALL -> "전체"
        KONKUK_TO_JUNGMUN -> "건입~중문"
        JUNGMUN_TO_EODAE -> "중문~어대"
        BACK_GATE -> "후문"
        FRONT_GATE -> "정문"
        GUUI_STATION -> "구의역"
    }

    companion object {
        fun normalize(values: Set<Location>): Set<Location> {
            return when {
                values.contains(ALL) -> setOf(ALL)
                values.isEmpty() -> setOf(ALL)
                else -> values
            }
        }

        fun toggle(
            current: Set<Location>,
            clicked: Location,
        ): Set<Location> {
            val normalized = normalize(current)

            return when (clicked) {
                ALL -> {
                    setOf(ALL)
                }

                else -> {
                    val withoutAll = normalized - ALL

                    val next = if (clicked in withoutAll) {
                        withoutAll - clicked
                    } else {
                        withoutAll + clicked
                    }

                    normalize(next)
                }
            }
        }
    }
}