package com.kus.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kus.designsystem.theme.KusTheme

/**
 * 기본 다이얼로그
 *
 * @param content 다이얼로그의 기본 내용
 * @param confirmText 확인 버튼명
 * @param modifier 수정자
 * @param subTitle 다이얼로그 상단에 보여지는 부제목명
 * @param onConfirmButtonClick 확인 버튼 클릭 시 호출되는 콜백
 * @param onDismissRequest 취소 버튼 클릭 시 호출되는 콜백
 */
@Composable
fun KusBasicDialog(
    content: @Composable () -> Unit,
    confirmText: String,
    modifier: Modifier = Modifier,
    subTitle: String = "",
    onConfirmButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp
        ) {
            Box(
                modifier = modifier
                    .width(300.dp)
                    .background(KusTheme.colors.c_FFFFFF, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (subTitle.isNotEmpty()) {
                        Text(
                            text = "안내",
                            style = KusTheme.typography.type16b,
                            color = KusTheme.colors.c_AAAAAA,
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    content()

                    Spacer(Modifier.height(20.dp))

                    KusButton(
                        enabled = true,
                        buttonName = confirmText,
                        textStyle = KusTheme.typography.type16m,
                        containerColor = KusTheme.colors.c_098C62,
                        roundedCornerShape = RoundedCornerShape(12.dp),
                        onClick = onConfirmButtonClick,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(6.dp))

                    KusButton(
                        enabled = true,
                        buttonName = "취소",
                        textStyle = KusTheme.typography.type16m,
                        containerColor = KusTheme.colors.c_AAAAAA,
                        borderColor = KusTheme.colors.c_AAAAAA,
                        roundedCornerShape = RoundedCornerShape(12.dp),
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
