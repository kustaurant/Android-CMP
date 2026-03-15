package com.kus.feature.draw.mapper

import com.kus.shared.domain.model.tier.filter.Cuisine
import kustaurant.shared.core.designsystem.generated.resources.img_category_all
import kustaurant.shared.core.designsystem.generated.resources.img_category_asian
import kustaurant.shared.core.designsystem.generated.resources.img_category_bakery
import kustaurant.shared.core.designsystem.generated.resources.img_category_beer
import kustaurant.shared.core.designsystem.generated.resources.img_category_benefit
import kustaurant.shared.core.designsystem.generated.resources.img_category_cafe
import kustaurant.shared.core.designsystem.generated.resources.img_category_chicken
import kustaurant.shared.core.designsystem.generated.resources.img_category_china
import kustaurant.shared.core.designsystem.generated.resources.img_category_hamburger_pizza
import kustaurant.shared.core.designsystem.generated.resources.img_category_japan
import kustaurant.shared.core.designsystem.generated.resources.img_category_korea
import kustaurant.shared.core.designsystem.generated.resources.img_category_meat
import kustaurant.shared.core.designsystem.generated.resources.img_category_salad
import kustaurant.shared.core.designsystem.generated.resources.img_category_seafood
import kustaurant.shared.core.designsystem.generated.resources.img_category_tteokbokki
import kustaurant.shared.core.designsystem.generated.resources.img_category_western
import org.jetbrains.compose.resources.DrawableResource
import kustaurant.shared.core.designsystem.generated.resources.Res as CoreRes

fun Cuisine.toImage(): DrawableResource = when (this) {
    Cuisine.ALL -> CoreRes.drawable.img_category_all
    Cuisine.KOREAN -> CoreRes.drawable.img_category_korea
    Cuisine.JAPANESE -> CoreRes.drawable.img_category_japan
    Cuisine.CHINESE -> CoreRes.drawable.img_category_china
    Cuisine.WESTERN -> CoreRes.drawable.img_category_western
    Cuisine.ASIAN -> CoreRes.drawable.img_category_asian
    Cuisine.MEAT -> CoreRes.drawable.img_category_meat
    Cuisine.CHICKEN -> CoreRes.drawable.img_category_chicken
    Cuisine.SEAFOOD -> CoreRes.drawable.img_category_seafood
    Cuisine.BURGER_PIZZA -> CoreRes.drawable.img_category_hamburger_pizza
    Cuisine.BUNSIK -> CoreRes.drawable.img_category_tteokbokki
    Cuisine.PUB -> CoreRes.drawable.img_category_beer
    Cuisine.CAFE_DESSERT -> CoreRes.drawable.img_category_cafe
    Cuisine.BAKERY -> CoreRes.drawable.img_category_bakery
    Cuisine.SALAD -> CoreRes.drawable.img_category_salad
    Cuisine.PARTNERSHIP -> CoreRes.drawable.img_category_benefit
}