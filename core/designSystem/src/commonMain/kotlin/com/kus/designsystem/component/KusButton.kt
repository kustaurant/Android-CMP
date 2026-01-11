package com.kus.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import kustaurant.core.designsystem.generated.resources.Res
import kustaurant.core.designsystem.generated.resources.ic_like
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 쿠스토랑 버튼
 *
 * @param enabled 버튼 활성화 여부
 * @param buttonName 버튼 내용
 * @param roundedCornerShape 버튼 테두리 모양
 * @param modifier 내용에 적용되는 수정자
 * @param textStyle 내용 텍스트 style(default: type18sb)
 * @param contentColor 내용 색상(default: White)
 * @param containerColor 버튼 배경 색상(default: c_43AB38)
 * @param borderColor 버튼 테두리 색상(default: c_43AB38)
 * @param contentPadding 내용과 버튼 사이 패딩(default: PaddingValues(vertical = 13.dp))
 * @param icon 버튼에 들어가는 좌측 아이콘(default: null)
 * @param isShadowVisible 새도우 포함 여부(default: false)
 * @param onClick 버튼 클릭 시 호출 되는 콜백 함수
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
    icon: ImageVector? = null,
    isShadowVisible: Boolean = false,
    onClick: () -> Unit,
) {
    Box {
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
            modifier = Modifier,
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
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(Modifier.width(10.dp))
                }

                Text(
                    text = buttonName,
                    style = textStyle,
                )
            }
        }
    }
}

@Preview
@Composable
private fun KusButtonPreview() {
    KusTheme {
        KusButton(
            buttonName = "랜덤 뽑기",
            enabled = false,
            roundedCornerShape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth(),
            borderColor = KusTheme.colors.c_E0E0E0,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun KusButtonPreview2() {
    KusTheme {
        KusButton(
            buttonName = "다시 뽑기",
            enabled = true,
            roundedCornerShape = RoundedCornerShape(100.dp),
            modifier = Modifier,
            textStyle = KusTheme.typography.type14r,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            icon = vectorResource(Res.drawable.ic_like),
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KusButtonPreview3() {
    KusTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            KusButton(
                buttonName = "카테고리 재설정",
                enabled = true,
                roundedCornerShape = RoundedCornerShape(100.dp),
                modifier = Modifier,
                textStyle = KusTheme.typography.type14r,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                contentColor = KusTheme.colors.c_43AB38,
                containerColor = Color.White,
                isShadowVisible = true,
                onClick = {},
            )
        }
    }
}
