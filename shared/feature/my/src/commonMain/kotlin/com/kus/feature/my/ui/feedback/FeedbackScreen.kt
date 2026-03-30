package com.kus.feature.my.ui.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.component.MyPageTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun FeedbackScreen(
    onBackClick: () -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: FeedbackViewModel = koinViewModel(),
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { message ->
            onShowMessage(message)
            if (!message.contains("오류")) {
                onBackClick()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF)
            .navigationBarsPadding()
            .imePadding(),
    ) {
        MyPageTopBar(
            title = "의견 보내기",
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f)
                    .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                    .padding(top = 10.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = KusTheme.typography.type14r,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f),
                    placeholder = {
                        Text(
                            text = "쿠스토랑에 대한 자유로운 의견들을 입력해주세요",
                            style = KusTheme.typography.type14r,
                            color = KusTheme.colors.c_AAAAAA,
                        )
                    },
                    maxLines = 8,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = KusTheme.colors.c_AAAAAA,
                        focusedBorderColor = KusTheme.colors.c_43AB38,
                        cursorColor = KusTheme.colors.c_666666,
                    ),
                    shape = RoundedCornerShape(8.dp),
                )
            }

            KusButton(
                enabled = text.isNotEmpty() && text.length >= 10 && text.length <= 500,
                buttonName = "의견 보내기",
                roundedCornerShape = RoundedCornerShape(50.dp),
                onClick = { viewModel.postFeedback(text) },
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}