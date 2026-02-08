package com.kus.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

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
    leftIcon: DrawableResource? = null,
    rightFirstIcon: DrawableResource? = null,
    rightSecondIcon: DrawableResource? = null,
    iconTint: Color? = null,
    onLeftClicked: () -> Unit = {},
    onRightFirstClicked: () -> Unit = {},
    onRightSecondClicked: () -> Unit = {},
    leftIconModifier: Modifier = Modifier,
    rightFirstIconModifier: Modifier = Modifier,
    rightSecondIconModifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    val iconTintColor: Color = iconTint ?: Color.Unspecified

    Box(
        modifier = modifier
    ) {
        // 가운데 컨텐츠
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            content()
        }

        // 왼쪽 아이콘
        if (leftIcon != null) {
            IconButton(
                onClick = onLeftClicked,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .then(leftIconModifier)
            ) {
                Icon(
                    painter = painterResource(leftIcon),
                    contentDescription = null,
                    tint = iconTintColor
                )
            }
        }

        // 오른쪽 아이콘들
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.End
        ) {
            if (rightFirstIcon != null) {
                IconButton(
                    onClick = onRightFirstClicked,
                    modifier = rightFirstIconModifier
                ) {
                    Icon(
                        painter = painterResource(rightFirstIcon),
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
                        painter = painterResource(rightSecondIcon),
                        contentDescription = null,
                        tint = iconTintColor
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "오른쪽 아이콘 1개")
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
