package com.kus.feature.evaluate.model

data class EvaluateRestaurant(
    val restaurantId: Long,
    val restaurantName: String,
    val mainTier: Int,
    val restaurantCuisine: String,
    val restaurantCuisineImgUrl: String,
    val restaurantPosition: String,
    val restaurantAddress: String,
    val situationList: ArrayList<String>,
    val partnershipInfo: String,
) {
    companion object {
        fun empty() = EvaluateRestaurant(
            restaurantId = 0,
            restaurantName = "",
            mainTier = 0,
            restaurantCuisine = "",
            restaurantCuisineImgUrl = "",
            restaurantPosition = "",
            restaurantAddress = "",
            situationList = arrayListOf(),
            partnershipInfo = "",
        )
    }
}
