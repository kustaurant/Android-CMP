package com.kus.feature.my.type

import kustaurant.shared.feature.my.generated.resources.Res
import kustaurant.shared.feature.my.generated.resources.ic_my_comment
import kustaurant.shared.feature.my.generated.resources.ic_saved_restaurant
import org.jetbrains.compose.resources.DrawableResource

enum class RestaurantMenu(
    override val title: String,
    override val iconRes: DrawableResource,
) : MenuItemUi {
    SAVED_RESTAURANT("저장한 맛집", Res.drawable.ic_saved_restaurant),
    CHECKED_RESTAURANT("내가 남긴 평가", Res.drawable.ic_my_comment),
}
