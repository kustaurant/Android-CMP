//package com.kus.feature.evaluate.component
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import com.kus.designsystem.theme.KusTheme
//
//@Composable
//fun EvaluationImage(
//
//) {
//    Box(
//        modifier = Modifier.fillMaxWidth()
//            .padding(horizontal = 20.dp)
//            .padding(top = 30.dp)
//            .border(
//                shape = RoundedCornerShape(16.dp),
//                width = 1.dp,
//                color = KusTheme.colors.c_AAAAAA
//            )
//    ) {
//        if (imageUri == null) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 20.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(ic_no_image),
//                    contentDescription = null,
//                    tint = TogedyTheme.colors.gray300
//                )
//
//                Text(
//                    text = "이미지 추가",
//                    style = TogedyTheme.typography.body12m.copy(
//                        color = TogedyTheme.colors.gray500
//                    ),
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//            }
//        } else {
//            Image(
//                painter = rememberAsyncImagePainter(model = imageUri),
//                contentDescription = "선택된 스터디 이미지",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(16f / 9f),
//                contentScale = ContentScale.Crop
//            )
//        }
//    }
//
//}