package com.kus.feature.evaluate.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kus.designsystem.theme.KusTheme

@Composable
fun EvaluationReview(
    evaluationComment: String,
    onCommentChange: (String) -> Unit
) {
    val hintText = "리뷰를 작성해주세요!\n\nex) 웨이팅 여부, 언제 방문 했는지, 식당의 분위기 등"

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
            .height(150.dp)
            .border(
                shape = RoundedCornerShape(16.dp),
                width = 1.dp,
                color = KusTheme.colors.c_AAAAAA
            )
    ) {

        TextField(
            value = evaluationComment,
            onValueChange = onCommentChange,
            placeholder = {
                Text(
                    text = hintText,
                    style = KusTheme.typography.type14r.copy(color = KusTheme.colors.c_AAAAAA)
                )
            },
            textStyle = KusTheme.typography.type14r.copy(color = KusTheme.colors.c_666666),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = KusTheme.colors.c_FFFFFF,
                unfocusedContainerColor = KusTheme.colors.c_FFFFFF,
                disabledContainerColor = KusTheme.colors.c_FFFFFF,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}