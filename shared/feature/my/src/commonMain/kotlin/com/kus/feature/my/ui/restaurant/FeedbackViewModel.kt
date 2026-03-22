package com.kus.feature.my.ui.restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kus.shared.domain.my.usecase.PostFeedbackUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FeedbackViewModel(
    private val postFeedbackUseCase: PostFeedbackUseCase,
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun postFeedback(content: String) = viewModelScope.launch {
        runCatching { postFeedbackUseCase(content) }
            .onSuccess { _eventFlow.emit(it)  }
            .onFailure { _eventFlow.emit("의견 보내기에 실패했습니다.") }
    }
}
