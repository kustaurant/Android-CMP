package com.kus.feature.my.ui.state

data class EditProfileUiState(
    val originalNickname: String = "",
    val originalEmail: String = "",
    val originalPhoneNumber: String = "",
    val nickname: String = "",
    val phoneNumber: String = "",
    val isPhoneNumberError: Boolean = false,
    val isButtonAvailable: Boolean = false,
)
