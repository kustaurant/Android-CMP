package com.kus.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme
import com.kus.designsystem.util.noRippleClickable
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 티어표 상단 필터 칩
 *
 * @param modifier chip 영역 수정자
 * @param chipName chip 이름
 * @param isSelected chip 선택 유무
 * @param onClick chip 클릭 시 호출 되는 콜백 함수
 */
@Composable
fun KusChip(
    modifier: Modifier = Modifier,
    chipName: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    val roundedCornerShape = RoundedCornerShape(100.dp)
    val mainColor =
        if (isSelected) KusTheme.colors.c_43AB38
        else KusTheme.colors.c_AAAAAA
    val backgroundColor =
        if (isSelected) KusTheme.colors.c_43AB38.copy(alpha = 0.2f)
        else Color.White

    Box(
        modifier = modifier
            .background(backgroundColor, roundedCornerShape)
            .noRippleClickable(onClick = onClick)
            .border(1.dp, mainColor, roundedCornerShape)
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Text(
            text = chipName,
            style = KusTheme.typography.type14r,
            color = mainColor,
        )
    }
}

@Preview
@Composable
private fun KusChipPreview() {
    var isSelected by remember { mutableStateOf(false) }

    KusTheme {
        KusChip(
            chipName = "전체",
            isSelected = isSelected,
            onClick = { isSelected = !isSelected },
        )
    }
}
