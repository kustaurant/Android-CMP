package com.kus.shared.domain.my.usecase

import com.kus.shared.domain.model.my.FavoriteResItem
import com.kus.shared.domain.my.repository.MyRestaurantRepository

class GetFavoriteResUseCase(
    private val repository: MyRestaurantRepository,
) {
    suspend operator fun invoke(): List<FavoriteResItem> {
        return repository.getFavoriteRes()
    }
}
