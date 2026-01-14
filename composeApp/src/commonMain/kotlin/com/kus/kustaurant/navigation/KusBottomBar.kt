package com.kus.kustaurant.navigation

import BottomBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kus.designsystem.navigation.BottomBar

@Composable
fun KusBottomBar(
    selectedKey: String,
    onNavigateToTab: (key: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        BottomBarItem(BottomTab.HOME.key, BottomTab.HOME.title, homeTabPainter()),
        BottomBarItem(BottomTab.DRAW.key, BottomTab.DRAW.title, drawTabPainter()),
        BottomBarItem(BottomTab.TIER.key, BottomTab.TIER.title, tierTabPainter()),
        BottomBarItem(BottomTab.COMMUNITY.key, BottomTab.COMMUNITY.title, communityTabPainter()),
        BottomBarItem(BottomTab.MY.key, BottomTab.MY.title, myTabPainter()),
    )

    BottomBar(
        items = items,
        selectedKey = selectedKey,
        onItemClick = { key -> onNavigateToTab(key) },
        modifier = modifier
    )
}
