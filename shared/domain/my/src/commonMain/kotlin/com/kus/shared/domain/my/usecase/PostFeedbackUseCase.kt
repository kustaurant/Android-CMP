package com.kus.shared.domain.my.usecase

import MyRepository

class PostFeedbackUseCase(
    private val repository: MyRepository,
) {
    suspend operator fun invoke(content: String): String {
        return repository.postFeedback(content)
    }
}
