package com.kus.feature.my.ui.type

import kustaurant.shared.feature.my.generated.resources.Res
import kustaurant.shared.feature.my.generated.resources.ic_my_comment
import kustaurant.shared.feature.my.generated.resources.ic_notice
import kustaurant.shared.feature.my.generated.resources.ic_terms
import org.jetbrains.compose.resources.DrawableResource

enum class CommunityMenu(
    override val title: String,
    override val iconRes: DrawableResource,
) : MenuItemUi {
    MY_ARTICLE("작성한 글", Res.drawable.ic_terms),
    MY_COMMENT("내가 남긴 댓글", Res.drawable.ic_my_comment),
    SCRAP_ARTICLE("스크랩한 글", Res.drawable.ic_notice),
}