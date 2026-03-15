package com.kus.data.draw.mapper

import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location

object DrawQueryMapper {
    fun Location.toApiCode(): String =
        when (this) {
            Location.ALL -> "ALL"
            Location.KONKUK_TO_JUNGMUN -> "L1"
            Location.JUNGMUN_TO_EODAE -> "L2"
            Location.BACK_GATE -> "L3"
            Location.FRONT_GATE -> "L4"
            Location.GUUI_STATION -> "L5"
        }

    fun Cuisine.toApiCode(): String =
        when (this) {
            Cuisine.ALL -> "ALL"
            Cuisine.KOREAN -> "KO"
            Cuisine.JAPANESE -> "JA"
            Cuisine.CHINESE -> "CH"
            Cuisine.WESTERN -> "WE"
            Cuisine.ASIAN -> "AS"
            Cuisine.MEAT -> "ME"
            Cuisine.CHICKEN -> "CK"
            Cuisine.SEAFOOD -> "SE"
            Cuisine.BURGER_PIZZA -> "HP"
            Cuisine.BUNSIK -> "BS"
            Cuisine.PUB -> "PU"
            Cuisine.CAFE_DESSERT -> "CA"
            Cuisine.BAKERY -> "BA"
            Cuisine.SALAD -> "SA"
            Cuisine.PARTNERSHIP -> "JH"
        }

    fun Set<Cuisine>.toCuisineQuery(): String {
        val set = toSet()

        return when {
            Cuisine.ALL in set -> "ALL"
            Cuisine.PARTNERSHIP in set -> "JH"
            else -> joinToString(",") { it.toApiCode() }
        }
    }

    fun Set<Location>.toLocationQuery(): String {
        val set = toSet()

        return when {
            Location.ALL in set -> "ALL"
            else -> joinToString(",") { it.toApiCode() }
        }
    }
}
