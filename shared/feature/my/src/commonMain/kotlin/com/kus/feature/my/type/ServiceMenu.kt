package com.kus.feature.my.type

import kustaurant.shared.feature.my.generated.resources.Res
import kustaurant.shared.feature.my.generated.resources.ic_feedback
import kustaurant.shared.feature.my.generated.resources.ic_notice
import kustaurant.shared.feature.my.generated.resources.ic_privacy_policy
import kustaurant.shared.feature.my.generated.resources.ic_terms
import org.jetbrains.compose.resources.DrawableResource

enum class ServiceMenu(
    override val title: String,
    override val iconRes: DrawableResource,
) : MenuItemUi {
    NOTICE("공지사항", Res.drawable.ic_notice),
    TERMS_OF_SERVICE("이용약관", Res.drawable.ic_terms),
    PRIVACY_POLICY("개인정보처리방침", Res.drawable.ic_privacy_policy),
    SEND_FEEDBACK("의견 보내기", Res.drawable.ic_feedback),
}
