package com.kus.kustaurant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.kus.feature.home.R as HomeR
import com.kus.feature.draw.R as DrawR
import com.kus.feature.tier.R as TierR
import com.kus.feature.community.R as CommunityR
import com.kus.feature.my.R as MyR

@Composable
actual fun homeTabPainter(): Painter = painterResource(HomeR.drawable.ic_home)
@Composable
actual fun drawTabPainter(): Painter =
    painterResource(DrawR.drawable.ic_draw)

@Composable
actual fun tierTabPainter(): Painter =
    painterResource(TierR.drawable.ic_tier)

@Composable
actual fun communityTabPainter(): Painter =
    painterResource(CommunityR.drawable.ic_community)

@Composable
actual fun myTabPainter(): Painter =
    painterResource(MyR.drawable.ic_my)