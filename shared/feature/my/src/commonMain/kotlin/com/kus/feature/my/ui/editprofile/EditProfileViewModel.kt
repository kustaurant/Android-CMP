package com.kus.feature.my.ui.editprofile

import androidx.lifecycle.ViewModel
import com.kus.feature.my.ui.state.EditProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditProfileViewModel(

) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun init(nickName: String, email: String, phoneNumber: String) {
        _uiState.value = EditProfileUiState(
            originalNickname = nickName,
            originalEmail = email,
            originalPhoneNumber = phoneNumber,
            nickname = nickName,
            phoneNumber = phoneNumber
        )
    }

    fun updateNickname(new: String) {
        _uiState.update { it.copy(nickname = new) }
        updateButtonAvailable()
    }

    fun updatePhoneNumber(new: String) {
        _uiState.update {
            it.copy(
                phoneNumber = new,
                isPhoneNumberError = isValidPhoneNumber(new),
            )
        }
        updateButtonAvailable()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val regex = Regex("^010\\d{8}$")
        return !regex.matches(phone)
    }

    private fun updateButtonAvailable() {
        val isAvailable =
            (uiState.value.nickname != uiState.value.originalNickname ||
                    uiState.value.phoneNumber != uiState.value.originalPhoneNumber) &&
                    !uiState.value.isPhoneNumberError

        _uiState.update { it.copy(isButtonAvailable = isAvailable) }
    }
}