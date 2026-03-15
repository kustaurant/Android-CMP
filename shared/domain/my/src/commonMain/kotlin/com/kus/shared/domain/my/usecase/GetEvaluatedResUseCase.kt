package com.kus.shared.domain.my.usecase

import com.kus.shared.domain.model.my.EvaluatedResItem
import com.kus.shared.domain.my.repository.MyRestaurantRepository

class GetEvaluatedResUseCase(
    private val repository: MyRestaurantRepository,
) {
    suspend operator fun invoke(): List<EvaluatedResItem> {
        return repository.getEvaluatedRes()
    }
}
