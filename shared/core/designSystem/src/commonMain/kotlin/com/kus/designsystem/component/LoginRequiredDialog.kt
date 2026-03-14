package com.kus.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable

/**
 * 미로그인시 기능제한 안내 오버레이
 *
 * @param modifier 다이얼로그 수정자
 * @param onLoginButtonClick 로그인 버튼 클릭 시 호출되는 콜백
 * @param onDismissRequest 취소 버튼 및 외부 영역 터치 시 호출되는 콜백
 */
@Composable
fun LoginRequiredOverlay(
    modifier: Modifier = Modifier,
    onLoginButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_000000.copy(alpha = 0.5f))
            .noRippleClickable(onDismissRequest),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = modifier
                .width(300.dp)
                .background(KusTheme.colors.c_FFFFFF, RoundedCornerShape(16.dp))
                .noRippleClickable {} // 팝업 내부 클릭 시 dismiss 방지
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "안내",
                    style = KusTheme.typography.type16b,
                    color = KusTheme.colors.c_AAAAAA,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "현재 서비스는 로그인 후\n이용할 수 있습니다",
                    style = KusTheme.typography.type16m,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(18.dp))

                KusButton(
                    enabled = true,
                    buttonName = "로그인 하러 가기",
                    textStyle = KusTheme.typography.type16m,
                    containerColor = KusTheme.colors.c_098C62,
                    roundedCornerShape = RoundedCornerShape(12.dp),
                    onClick = onLoginButtonClick,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "나중에 하기",
                    style = KusTheme.typography.type14r,
                    color = KusTheme.colors.c_AAAAAA,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.noRippleClickable(onDismissRequest),
                )
            }
        }
    }
}