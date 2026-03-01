package com.kus.feature.my.ui.type

import kustaurant.shared.feature.my.generated.resources.Res
import kustaurant.shared.feature.my.generated.resources.ic_delete_account
import kustaurant.shared.feature.my.generated.resources.ic_logout
import org.jetbrains.compose.resources.DrawableResource

enum class AccountMenu(
    override val title: String,
    override val iconRes: DrawableResource,
    override val count: Int? = null,
) : MenuItemUi {
    LOGOUT("로그아웃", Res.drawable.ic_logout),
    DELETE_ACCOUNT("회원탈퇴", Res.drawable.ic_delete_account),
}
