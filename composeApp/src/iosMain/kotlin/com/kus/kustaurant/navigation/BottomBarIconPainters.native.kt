package com.kus.kustaurant.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import kustaurant.shared.feature.community.generated.resources.ic_community
import kustaurant.shared.feature.draw.generated.resources.ic_draw
import kustaurant.shared.feature.home.generated.resources.ic_home
import kustaurant.shared.feature.my.generated.resources.ic_my
import kustaurant.shared.feature.tier.generated.resources.ic_tier
import org.jetbrains.compose.resources.painterResource
import kustaurant.shared.feature.home.generated.resources.Res as HomeRes
import kustaurant.shared.feature.draw.generated.resources.Res as DrawRes
import kustaurant.shared.feature.tier.generated.resources.Res as TierRes
import kustaurant.shared.feature.community.generated.resources.Res as CommunityRes
import kustaurant.shared.feature.my.generated.resources.Res as MyRes

@Composable
actual fun homeTabPainter(): Painter = painterResource(HomeRes.drawable.ic_home)
@Composable
actual fun drawTabPainter(): Painter = painterResource(DrawRes.drawable.ic_draw)
@Composable
actual fun tierTabPainter(): Painter = painterResource(TierRes.drawable.ic_tier)
@Composable
actual fun communityTabPainter(): Painter = painterResource(CommunityRes.drawable.ic_community)
@Composable
actual fun myTabPainter(): Painter = painterResource(MyRes.drawable.ic_my)