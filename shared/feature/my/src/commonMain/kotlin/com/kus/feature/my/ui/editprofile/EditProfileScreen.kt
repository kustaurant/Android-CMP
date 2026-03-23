package com.kus.feature.my.ui.editprofile

import UiState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kus.designsystem.component.KusBasicTextField
import com.kus.designsystem.component.KusButton
import com.kus.designsystem.theme.KusTheme
import com.kus.feature.my.component.MyPageTopBar
import com.kus.feature.my.event.MyNavigationEvent
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileRoute(
    nickName: String,
    email: String,
    phoneNumber: String,
    onShowMessage: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: EditProfileViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        launch {
            viewModel.errorMsgEvent.collect { onShowMessage(it) }
        }
        launch {
            viewModel.navigationEvent.collect { event ->
                when (event) {
                    is MyNavigationEvent.NavigateToUp -> onBackClick()
                    else -> {}
                }
            }
        }

        viewModel.init(nickName, email, phoneNumber)
    }

    Box {
        EditProfileScreen(
            nickName = uiState.value.nickname,
            email = uiState.value.originalEmail,
            phone = uiState.value.phoneNumber,
            isPhoneNumberError = uiState.value.isPhoneNumberError,
            isButtonAvailable = uiState.value.isButtonAvailable,
            onBackClick = onBackClick,
            onNicknameChanged = viewModel::updateNickname,
            onPhoneNumberChanged = viewModel::updatePhoneNumber,
            onEditButtonClick = viewModel::updateProfileInfo,
        )

        if (uiState.value.uiState is UiState.Loading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(KusTheme.colors.c_000000.copy(alpha = 0.3f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {},
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun EditProfileScreen(
    nickName: String,
    email: String,
    phone: String,
    isPhoneNumberError: Boolean,
    isButtonAvailable: Boolean,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNicknameChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onEditButtonClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KusTheme.colors.c_FFFFFF)
            .navigationBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) {
        MyPageTopBar(
            title = "프로필 수정",
            onBackClick = onBackClick,
        )

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            KusBasicTextField(
                title = "닉네임",
                value = nickName,
                subtitle = "닉네임은 30일에 한번만 변경 가능합니다",
                placeholder = "닉네임을 입력해주세요",
                onValueChange = onNicknameChanged,
            )

            Spacer(Modifier.height(14.dp))

            KusBasicTextField(
                title = "이메일",
                value = email,
                enabled = false,
            )

            Spacer(Modifier.height(14.dp))

            KusBasicTextField(
                title = "연락처",
                value = phone,
                subtitle = "이벤트 쿠폰 수신을 위해 핸드폰 번호를 입력해주세요",
                placeholder = "연락처를 입력해주세요(‘-’제외)",
                onValueChange = onPhoneNumberChanged,
            )

            if (isPhoneNumberError) {
                Spacer(Modifier.height(4.dp))

                Text(
                    text = "숫자 11자리로 입력해주세요.",
                    style = KusTheme.typography.type13r,
                    color = KusTheme.colors.c_FF0000,
                )
            }

            Spacer(Modifier.weight(1f))

            KusButton(
                enabled = isButtonAvailable,
                buttonName = "프로필 수정하기",
                roundedCornerShape = RoundedCornerShape(30.dp),
                onClick = onEditButtonClick,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}