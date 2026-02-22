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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

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
    rightSecondIconModifier: Modifier = Modifier,
    content: @Composable (() -> Unit) = {},
) {
    var leftWidthPx by remember { mutableIntStateOf(0) }
    var rightWidthPx by remember { mutableIntStateOf(0) }
    val iconTintColor: Color = iconTint ?: Color.Unspecified

    val density = LocalDensity.current
    val leftPadding = with(density) { leftWidthPx.toDp() }
    val rightPadding = with(density) { rightWidthPx.toDp() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // 중앙 컨텐츠: 좌/우 실제 너비만큼 패딩을 줘서 절대 침범하지 않음
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = leftPadding, end = rightPadding),
            contentAlignment = Alignment.Center
        ) {
            content()
        }

        // 왼쪽 아이콘 영역 (실제 너비 측정)
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
            // 아이콘이 없으면 0으로
            LaunchedEffect(Unit) { leftWidthPx = 0 }
        }

        // 오른쪽 아이콘들 영역 (실제 너비 측정)
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onSizeChanged { rightWidthPx = it.width },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if (rightFirstIcon != null) {
                IconButton(
                    onClick = onRightFirstClicked,
                    modifier = rightFirstIconModifier
                ) {
                    Icon(
                        painter = rightFirstIcon,
                        contentDescription = null,
                        tint = iconTintColor
                    )
                }
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

        // 오른쪽 아이콘이 둘 다 없으면 Row 너비가 0이 되지만,
        // 혹시 레이아웃 캐시 때문에 남아있을 수 있으니 안전 처리
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
