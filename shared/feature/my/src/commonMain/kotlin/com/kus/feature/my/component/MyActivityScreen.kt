package com.kus.feature.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.type.CommunityMenu
import com.kus.feature.my.type.RestaurantMenu

@Composable
internal fun MyActivityScreen(
    modifier: Modifier = Modifier,
    onSavedRestClick: () -> Unit,
    onCheckedRestClick: () -> Unit,
    onMyArticleClick: () -> Unit,
    onMyCommentClick: () -> Unit,
    onScrapClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_F3F3F3),
    ) {
        item {
            MenuList(
                title = "식당",
                items = RestaurantMenu.entries,
                onClick = { menu ->
                    when (menu) {
                        RestaurantMenu.SAVED_RESTAURANT -> onSavedRestClick()
                        RestaurantMenu.CHECKED_RESTAURANT -> onCheckedRestClick()
                    }
                }
            )
        }

        item {
            MenuList(
                title = "커뮤니티",
                items = CommunityMenu.entries,
                onClick = { menu ->
                    when (menu) {
                        CommunityMenu.MY_ARTICLE -> onMyArticleClick()
                        CommunityMenu.MY_COMMENT -> onMyCommentClick()
                        CommunityMenu.SCRAP_ARTICLE -> onScrapClick()
                    }
                }
            )
        }
    }
}