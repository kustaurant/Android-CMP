package com.kus.feature.community.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.kus.designsystem.component.KusChip
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import com.kus.domain.community.model.ListSortType
import com.kus.domain.community.model.PostCategory
import kustaurant.shared.feature.community.generated.resources.Res
import kustaurant.shared.feature.community.generated.resources.ic_drop_down
import org.jetbrains.compose.resources.painterResource

@Composable
fun CommunityListFilterBar(
    selectedBoard: PostCategory,
    onBoardClick: () -> Unit,
    boardExpanded: Boolean,
    boards: List<PostCategory>,
    onBoardSelect: (PostCategory) -> Unit,
    onBoardDismiss: () -> Unit,
    sortType: ListSortType,
    onSortChange: (ListSortType) -> Unit,
) {
    val density = LocalDensity.current
    var anchorHeightPx by remember { mutableIntStateOf(0) }
    val extraOffsetPx = with(density) { 18.dp.roundToPx() }
    val orderedBoards = remember(selectedBoard, boards) {
        listOf(selectedBoard) + boards.filter { it != selectedBoard }
    }

    Column {
        Row(
            modifier = Modifier
                .background(color = KusTheme.colors.c_FFFFFF)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Row(
                    modifier = Modifier
                        .noRippleClickable(onBoardClick)
                        .onSizeChanged { anchorHeightPx = it.height },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedBoard.displayName,
                        style = KusTheme.typography.type14r,
                        color = KusTheme.colors.c_43AB38
                    )
                    Spacer(Modifier.width(4.dp))
                    Image(
                        painter = painterResource(Res.drawable.ic_drop_down),
                        contentDescription = "드롭다운 메뉴 아이콘입니다.",
                        modifier = Modifier.size(width = 14.dp, height = 9.dp)
                    )
                }

                if (boardExpanded) {
                    Popup(
                        alignment = Alignment.TopStart,
                        offset = IntOffset(x = 0, y = -anchorHeightPx + extraOffsetPx),
                        onDismissRequest = onBoardDismiss
                    ) {
                        Box(
                            modifier = Modifier
                                .shadow(6.dp, RoundedCornerShape(10.dp))
                                .background(KusTheme.colors.c_E0E0E0, RoundedCornerShape(10.dp))
                                .wrapContentWidth()
                                .width(IntrinsicSize.Max)
                        ) {
                            Column(modifier = Modifier.wrapContentWidth()) {
                                orderedBoards.forEach { category ->
                                    val isSelected = category == selectedBoard

                                    Text(
                                        text = category.displayName,
                                        color = if (isSelected) KusTheme.colors.c_AAAAAA else KusTheme.colors.c_666666,
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(horizontal = 16.dp, vertical = 10.dp)
                                            .let { m ->
                                                if (!isSelected) m.noRippleClickable {
                                                    onBoardSelect(category)
                                                } else m
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                KusChip(
                    chipName = "최신순",
                    isSelected = sortType == ListSortType.LATEST,
                    onClick = { onSortChange(ListSortType.LATEST) },
                    modifier = Modifier.height(29.dp)
                )

                KusChip(
                    chipName = "인기순",
                    isSelected = sortType == ListSortType.POPULARITY,
                    onClick = { onSortChange(ListSortType.POPULARITY) },
                    modifier = Modifier.height(29.dp)
                )
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = KusTheme.colors.c_E0E0E0,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
