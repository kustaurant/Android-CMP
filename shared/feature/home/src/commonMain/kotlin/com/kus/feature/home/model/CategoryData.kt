package com.kus.feature.home.model

import com.kus.shared.domain.model.tier.filter.Cuisine
import kustaurant.shared.feature.home.generated.resources.Res
import kustaurant.shared.feature.home.generated.resources.img_category_all
import kustaurant.shared.feature.home.generated.resources.img_category_asian
import kustaurant.shared.feature.home.generated.resources.img_category_bakery
import kustaurant.shared.feature.home.generated.resources.img_category_beer
import kustaurant.shared.feature.home.generated.resources.img_category_benefit
import kustaurant.shared.feature.home.generated.resources.img_category_cafe
import kustaurant.shared.feature.home.generated.resources.img_category_chicken
import kustaurant.shared.feature.home.generated.resources.img_category_china
import kustaurant.shared.feature.home.generated.resources.img_category_hamburger_pizza
import kustaurant.shared.feature.home.generated.resources.img_category_japan
import kustaurant.shared.feature.home.generated.resources.img_category_korea
import kustaurant.shared.feature.home.generated.resources.img_category_meat
import kustaurant.shared.feature.home.generated.resources.img_category_salad
import kustaurant.shared.feature.home.generated.resources.img_category_seafood
import kustaurant.shared.feature.home.generated.resources.img_category_tteokbokki
import kustaurant.shared.feature.home.generated.resources.img_category_western
import org.jetbrains.compose.resources.DrawableResource

enum class Category(
    val image: DrawableResource,
    val title: String,
    val id: String,
    val cuisine: Cuisine,
) {
    ALL(Res.drawable.img_category_all, "전체", "ALL", Cuisine.ALL),
    KOREA(Res.drawable.img_category_korea, "한식", "KO", Cuisine.KOREAN),
    JAPAN(Res.drawable.img_category_japan, "일식", "JA", Cuisine.JAPANESE),
    CHINA(Res.drawable.img_category_china, "중식", "CH", Cuisine.CHINESE),
    WESTERN(Res.drawable.img_category_western, "양식", "WE", Cuisine.WESTERN),
    ASIAN(Res.drawable.img_category_asian, "아시안", "AS", Cuisine.ASIAN),
    MEAT(Res.drawable.img_category_meat, "고기", "ME", Cuisine.MEAT),
    SEAFOOD(Res.drawable.img_category_seafood, "해산물", "SE", Cuisine.SEAFOOD),
    CHICKEN(Res.drawable.img_category_chicken, "치킨", "CK", Cuisine.CHICKEN),
    HAMBURGER_PIZZA(Res.drawable.img_category_hamburger_pizza, "햄버거/피자", "HP", Cuisine.BURGER_PIZZA),
    TTEOKBOKKI(Res.drawable.img_category_tteokbokki, "분식", "BS", Cuisine.BUNSIK),
    PUB(Res.drawable.img_category_beer, "술집", "PU", Cuisine.PUB),
    CAFE_DESSERT(Res.drawable.img_category_cafe, "카페/디저트", "CA", Cuisine.CAFE_DESSERT),
    BAKERY(Res.drawable.img_category_bakery, "베이커리", "BA", Cuisine.BAKERY),
    SALAD(Res.drawable.img_category_salad, "샐러드", "SA", Cuisine.SALAD),
    BENEFIT(Res.drawable.img_category_benefit, "제휴업체", "JH", Cuisine.PARTNERSHIP),
}
