package com.kus.feature.my.ui.type

import org.jetbrains.compose.resources.DrawableResource

interface MenuItemUi {
    val title: String
    val iconRes: DrawableResource
    val count: Int?
}