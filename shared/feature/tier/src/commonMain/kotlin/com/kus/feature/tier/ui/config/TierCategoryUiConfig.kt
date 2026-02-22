package com.kus.feature.tier.ui.config

import com.kus.shared.domain.model.tier.filter.Cuisine
import com.kus.shared.domain.model.tier.filter.Location
import com.kus.shared.domain.model.tier.filter.Situation

object TierCategoryUiConfig {

    val cuisineItems = listOf(
        Cuisine.ALL,
        Cuisine.KOREAN,
        Cuisine.JAPANESE,
        Cuisine.CHINESE,
        Cuisine.WESTERN,
        Cuisine.ASIAN,
        Cuisine.MEAT,
        Cuisine.CHICKEN,
        Cuisine.SEAFOOD,
        Cuisine.BURGER_PIZZA,
        Cuisine.BUNSIK,
        Cuisine.PUB,
        Cuisine.CAFE_DESSERT,
        Cuisine.BAKERY,
        Cuisine.SALAD,
        Cuisine.PARTNERSHIP,
    )

    val situationItems = listOf(
        Situation.ALL,
        Situation.SOLO,
        Situation.TWO_TO_FOUR,
        Situation.FIVE_OR_MORE,
        Situation.COMPANY_DINNER,
        Situation.DELIVERY,
        Situation.LATE_NIGHT,
        Situation.INVITE_FRIENDS,
        Situation.DATE,
        Situation.BLIND_DATE,
    )

    val locationItems = listOf(
        Location.ALL,
        Location.KONKUK_TO_JUNGMUN,
        Location.JUNGMUN_TO_EODAE,
        Location.BACK_GATE,
        Location.FRONT_GATE,
        Location.GUUI_STATION,
    )
}
