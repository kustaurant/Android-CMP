package com.kus.shared.data.tier.mapper

import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation

fun Cuisine.toApiCode(): String = when (this) {
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

fun Situation.toApiCode(): String = when (this) {
    Situation.ALL -> "ALL"
    Situation.SOLO -> "1"
    Situation.TWO_TO_FOUR -> "2"
    Situation.FIVE_OR_MORE -> "3"
    Situation.COMPANY_DINNER -> "4"
    Situation.DELIVERY -> "5"
    Situation.LATE_NIGHT -> "6"
    Situation.INVITE_FRIENDS -> "7"
    Situation.DATE -> "8"
    Situation.BLIND_DATE -> "9"
}

fun Location.toApiCode(): String = when (this) {
    Location.ALL -> "ALL"
    Location.KONKUK_TO_JUNGMUN -> "L1"
    Location.JUNGMUN_TO_EODAE -> "L2"
    Location.BACK_GATE -> "L3"
    Location.FRONT_GATE -> "L4"
    Location.GUUI_STATION -> "L5"
}

fun Set<Cuisine>.toCuisineQuery(): String =
    Cuisine.normalize(this).joinToString(",") { it.toApiCode() }

fun Set<Situation>.toSituationQuery(): String =
    Situation.normalize(this).joinToString(",") { it.toApiCode() }

fun Set<Location>.toLocationQuery(): String =
    Location.normalize(this).joinToString(",") { it.toApiCode() }