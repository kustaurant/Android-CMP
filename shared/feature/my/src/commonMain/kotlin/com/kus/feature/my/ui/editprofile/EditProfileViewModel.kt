package com.kus.feature.my.ui.editprofile

import UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.feature.my.event.MyNavigationEvent
import com.kus.feature.my.state.EditProfileUiState
import com.kus.shared.domain.my.usecase.PatchProfileInfoUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val patchProfileInfoUseCase: PatchProfileInfoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<MyNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()
    private val _errorMsgEvent = MutableSharedFlow<String>()
    val errorMsgEvent = _errorMsgEvent.asSharedFlow()

    fun init(nickName: String, email: String, phoneNumber: String) {
        _uiState.value = EditProfileUiState(
            originalNickname = nickName,
            originalEmail = email,
            originalPhoneNumber = phoneNumber,
            nickname = nickName,
            phoneNumber = phoneNumber
        )
    }

    fun updateProfileInfo() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = UiState.Loading) }
        runCatching {
            with(uiState.value) {
                val phoneNumber = phoneNumber?.trim()?.ifEmpty { null }
                val name = if (originalNickname==nickname) null else nickname
                patchProfileInfoUseCase(name, phoneNumber)
            }
        }
            .onSuccess {
                _uiState.update { it.copy(uiState = UiState.Success(Unit)) }
                _navigationEvent.emit(MyNavigationEvent.NavigateToUp)
                _errorMsgEvent.emit("프로필이 수정되었습니다.")
            }
            .onFailure {
                _uiState.update { it.copy(uiState = UiState.Success(Unit)) }
                _errorMsgEvent.emit("오류가 발생했습니다. 다시 시도해주세요.")
            }
    }

    fun updateNickname(new: String) {
        _uiState.update { it.copy(nickname = new) }
        updateButtonAvailable()
    }

    fun updatePhoneNumber(new: String) {
        _uiState.update {
            it.copy(
                phoneNumber = new.ifEmpty { null },
                isPhoneNumberError = isValidPhoneNumber(new),
            )
        }
        updateButtonAvailable()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val regex = Regex("^010\\d{8}$")
        return !(regex.matches(phone) || phone.isEmpty())
    }

    private fun updateButtonAvailable() {
        val isAvailable =
            (uiState.value.nickname != uiState.value.originalNickname &&
                    uiState.value.nickname.length in 2..10) ||
                    uiState.value.phoneNumber == null ||
                    (uiState.value.phoneNumber != uiState.value.originalPhoneNumber &&
                            !uiState.value.isPhoneNumberError)

        _uiState.update { it.copy(isButtonAvailable = isAvailable) }
    }
}