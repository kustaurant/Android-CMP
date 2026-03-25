package com.kus.kustaurant.navigation

import BottomBarItem
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kus.designsystem.navigation.BottomBar
import com.kus.designsystem.navigation.BottomBarShadow

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
    val shadowHeight = 8.dp

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomBarShadow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(shadowHeight)
                .offset(y = -shadowHeight)
                .zIndex(1f)
        )

        BottomBar(
            items = items,
            selectedKey = selectedKey,
            onItemClick = { key -> onNavigateToTab(key) },
            modifier = modifier
        )
    }
}
