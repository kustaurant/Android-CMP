package com.kus.feature.my.state

import UiState

data class EditProfileUiState(
    val uiState: UiState<Unit> = UiState.Idle,
    val originalNickname: String = "",
    val originalEmail: String = "",
    val originalPhoneNumber: String = "",
    val nickname: String = "",
    val phoneNumber: String = "",
    val isPhoneNumberError: Boolean = false,
    val isButtonAvailable: Boolean = false,
)
