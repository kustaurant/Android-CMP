package com.kus.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme


/**
 * 쿠스토랑 버튼
 *
 * Material Button을 기반으로 한 커스텀 버튼 컴포넌트입니다.
 * 텍스트를 중앙 기준으로 정렬하고 좌/우 아이콘을 선택적으로 표시할 수 있습니다.
 *
 * 기본적으로 아이콘이 없을 경우 텍스트만 중앙에 표시되며,
 * 아이콘이 있을 경우 텍스트와 자연스럽게 나란히 배치됩니다.
 *
 * @param enabled 버튼 활성화 여부
 * @param buttonName 버튼에 표시될 텍스트
 * @param roundedCornerShape 버튼 모서리 형태
 * @param modifier 버튼 전체에 적용되는 Modifier
 * @param textStyle 버튼 텍스트 스타일(default: type18sb)
 * @param contentColor 버튼 내용(텍스트/아이콘) 색상(default: White)
 * @param containerColor 버튼 배경 색상(default: c_43AB38)
 * @param borderColor 버튼 테두리 색상(default: c_43AB38)
 * @param contentPadding 버튼 내부 패딩(default: PaddingValues(vertical = 13.dp))
 * @param leftIcon 버튼 좌측 아이콘 Painter(default: null)
 * @param rightIcon 버튼 우측 아이콘 Painter(default: null)
 * @param leftIconModifier 좌측 아이콘 Modifier (padding/offset 등 커스터마이징 가능)
 * @param rightIconModifier 우측 아이콘 Modifier (padding/offset 등 커스터마이징 가능)
 * @param iconSize 아이콘 크기(default: 20.dp)
 * @param isShadowVisible 버튼 그림자 표시 여부(default: false)
 * @param onClick 버튼 클릭 시 호출되는 콜백 함수
 */
@Composable
fun KusButton(
    enabled: Boolean,
    buttonName: String,
    roundedCornerShape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KusTheme.typography.type18sb,
    contentColor: Color = Color.White,
    containerColor: Color = KusTheme.colors.c_43AB38,
    borderColor: Color = KusTheme.colors.c_43AB38,
    contentPadding: PaddingValues = PaddingValues(vertical = 13.dp),
    leftIcon: Painter? = null,
    rightIcon: Painter? = null,
    leftIconModifier: Modifier = Modifier,
    rightIconModifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    isShadowVisible: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        if (isShadowVisible) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shadow(
                        elevation = 4.dp,
                        shape = roundedCornerShape,
                        ambientColor = Color.Transparent,
                    )
            )
        }

        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = roundedCornerShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = KusTheme.colors.c_E0E0E0,
                disabledContentColor = KusTheme.colors.c_AAAAAA,
            ),
            border = BorderStroke(1.dp, borderColor),
            contentPadding = contentPadding,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                leftIcon?.let {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        modifier = leftIconModifier.size(iconSize)
                    )

                    Spacer(Modifier.width(6.dp))
                }

                Text(
                    text = buttonName,
                    style = textStyle
                )

                rightIcon?.let {

                    Spacer(Modifier.width(6.dp))

                    Icon(
                        painter = it,
                        contentDescription = null,
                        modifier = rightIconModifier.size(iconSize)
                    )
                }
            }
        }
    }
}


//@Preview
//@Composable
//private fun KusButtonPreview() {
//    KusTheme {
//        KusButton(
//            buttonName = "랜덤 뽑기",
//            enabled = false,
//            roundedCornerShape = RoundedCornerShape(50.dp),
//            modifier = Modifier.fillMaxWidth(),
//            borderColor = KusTheme.colors.c_E0E0E0,
//            onClick = {},
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun KusButtonPreview2() {
//    KusTheme {
//        KusButton(
//            buttonName = "다시 뽑기",
//            enabled = true,
//            roundedCornerShape = RoundedCornerShape(100.dp),
//            modifier = Modifier,
//            textStyle = KusTheme.typography.type14r,
//            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
//            icon = vectorResource(Res.drawable.ic_like),
//            onClick = {},
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun KusButtonPreview3() {
//    KusTheme {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//        ) {
//            KusButton(
//                buttonName = "카테고리 재설정",
//                enabled = true,
//                roundedCornerShape = RoundedCornerShape(100.dp),
//                modifier = Modifier,
//                textStyle = KusTheme.typography.type14r,
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
//                contentColor = KusTheme.colors.c_43AB38,
//                containerColor = Color.White,
//                isShadowVisible = true,
//                onClick = {},
//            )
//        }
//    }
//}
