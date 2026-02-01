package com.kus.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

/**
 * 카테고리 버튼
 *
 * @param modifier 수정자
 * @param categoryName 카테고리명
 * @param categoryImage 카테고리 이미지 ImageVector
 * @param isSelected 버튼 선택 유무 (default : false)
 * @param onClick 버튼 클릭 시 호출 되는 콜백 함수
 */
@Composable
fun KusCategoryImageButton(
    modifier: Modifier = Modifier,
    categoryName: String,
    categoryImage: Painter,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    val roundedCornerShape = RoundedCornerShape(15.dp)
    val borderColor =
        if (isSelected) KusTheme.colors.c_43AB38
        else KusTheme.colors.c_F5F5F5

    Box(
        modifier = modifier
            .padding(2.dp)
            .wrapContentSize(),
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(15.dp),
                        ambientColor = borderColor.copy(alpha = 0.6f),
                    )
            )
        }

        Column(
            modifier = Modifier
                .border(width = 1.dp, color = borderColor, shape = roundedCornerShape)
                .clip(roundedCornerShape)
                .background(KusTheme.colors.c_F5F5F5)
                .clickable(onClick = onClick)
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = categoryImage,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
            )

            Text(
                text = categoryName,
                style = KusTheme.typography.type11m,
                color = KusTheme.colors.c_323232,
            )
        }
    }
}

//@Preview
//@Composable
//private fun KusCategoryImageButtonPreview() {
//    KusTheme {
//        KusCategoryImageButton(
//            categoryName = "전체",
//            categoryImage = vectorResource(Res.drawable.ic_like),
//            modifier = Modifier,
//            onClick = {},
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun KusCategoryImageButtonSelectablePreview() {
//    var isSelected by remember { mutableStateOf(false) }
//
//    KusTheme {
//        KusCategoryImageButton(
//            categoryName = "전체",
//            categoryImage = vectorResource(Res.drawable.ic_like),
//            isSelected = isSelected,
//            modifier = Modifier,
//            onClick = { isSelected = !isSelected },
//        )
//    }
//}
