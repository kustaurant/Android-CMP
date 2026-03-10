package com.kus.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import com.kus.designsystem.util.noRippleClickable

/**
 * 쿠스토랑 top bar
 * @param modifier : 전체 modifier
 * @param leftIcon : 좌측 아이콘
 * @param rightFirstIcon : 우측 왼쪽 첫번째 아이콘
 * @param rightSecondIcon : 우측 오른쪽 두번째 아이콘
 * @param onLeftClicked : 좌측 아이콘 클릭 로직
 * @param onRightFirstClicked : 우측 왼쪽 첫번째 아이콘 클릭 로직
 * @param onRightSecondClicked : 우측 오른쪽 첫번째 아이콘 클릭 로직
 * @param leftIconModifier : 좌측 아이콘 modifier
 * @param rightFirstIconModifier : 우측 왼쪽 첫번째 아이콘 modifier
 * @param rightSecondIconModifier : 우측 오른쪽 두번째 아이콘 modifier
 * @param content : 가운데 표시되는 content
 */
@Composable
fun KusTopBar(
    modifier: Modifier = Modifier,
    leftIcon: Painter? = null,
    rightFirstIcon: Painter? = null,
    rightSecondIcon: Painter? = null,
    onLeftClicked: () -> Unit = {},
    iconTint: Color? = null,
    onRightFirstClicked: () -> Unit = {},
    onRightSecondClicked: () -> Unit = {},
    leftIconModifier: Modifier = Modifier,
    rightFirstIconModifier: Modifier = Modifier,
    onRightFirstIconBoundsChanged: ((Rect) -> Unit)? = null,
    rightSecondIconModifier: Modifier = Modifier,
    content: @Composable (() -> Unit) = {},
) {
    var leftWidthPx by remember { mutableIntStateOf(0) }
    var rightWidthPx by remember { mutableIntStateOf(0) }
    val iconTintColor: Color = iconTint ?: Color.Unspecified

    val density = LocalDensity.current
    val leftWidth = with(density) { leftWidthPx.toDp() }
    val rightWidth = with(density) { rightWidthPx.toDp() }

    val safeSidePadding = maxOf(leftWidth, rightWidth)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = safeSidePadding),
            contentAlignment = Alignment.Center
        ) {
            content()
        }

        if (leftIcon != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .onSizeChanged { leftWidthPx = it.width }
            ) {
                IconButton(
                    onClick = onLeftClicked,
                    modifier = leftIconModifier
                ) {
                    Icon(
                        painter = leftIcon,
                        contentDescription = null,
                        tint = iconTintColor
                    )
                }
            }
        } else {
            LaunchedEffect(leftIcon) { leftWidthPx = 0 }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onSizeChanged { rightWidthPx = it.width },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if (rightFirstIcon != null) {
                Icon(
                    painter = rightFirstIcon,
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = rightFirstIconModifier
                        .onGloballyPositioned { coords ->
                            onRightFirstIconBoundsChanged?.invoke(coords.boundsInWindow())
                        }
                        .noRippleClickable { onRightFirstClicked() }
                )
            }
            if (rightSecondIcon != null) {
                IconButton(
                    onClick = onRightSecondClicked,
                    modifier = rightSecondIconModifier
                ) {
                    Icon(
                        painter = rightSecondIcon,
                        contentDescription = null,
                        tint = iconTintColor
                    )
                }
            }
        }
        LaunchedEffect(rightFirstIcon, rightSecondIcon) {
            if (rightFirstIcon == null && rightSecondIcon == null) rightWidthPx = 0
        }
    }
}

//@Composable
//fun KusTopBarPreviewOneRightIcon(){
//    KusTheme {
//        KusTopBar(
//            modifier = Modifier
//                .fillMaxWidth(),
//            leftIcon = Icons.Default.ArrowBack,
//            rightFirstIcon = Icons.Default.Search,
//            content = {
//                Text("제목")
//            }
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "오른쪽 아이콘 2개")
//@Composable
//fun KusTopBarPreviewTwoRightIcons(){
//    KusTheme {
//        KusTopBar(
//            modifier = Modifier
//                .fillMaxWidth(),
//            leftIcon = Icons.Default.ArrowBack,
//            rightFirstIcon = Icons.Default.Search,
//            rightSecondIcon = Icons.Default.Menu,
//            content = {
//                Text("제목")
//            }
//        )
//    }
//}
