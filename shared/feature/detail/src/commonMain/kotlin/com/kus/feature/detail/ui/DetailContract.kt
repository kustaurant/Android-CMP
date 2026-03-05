package com.kus.feature.detail.ui

data class DetailUiState(
    val restaurant: DetailRestaurant = DetailRestaurant.empty(),
)

data class DetailReviewUiState(
    val reviewList: List<DetailReview> = emptyList(),
    val isLoading: Boolean = false,
    val hasLoaded: Boolean = false,
    val sort: ReviewSort = ReviewSort.Popular,
)

enum class ReviewSort {
    Popular,
    Latest
}

data class DetailRestaurant(
    val restaurantId: Int,
    val restaurantImgUrl: String,
    val mainTier: Int,
    val isTempTier: Boolean,
    val restaurantCuisine: String,
    val restaurantCuisineImgUrl: String,
    val restaurantPosition: String,
    val restaurantName: String,
    val restaurantAddress: String,
    val isOpen: Boolean,
    val businessHours: String,
    val naverMapUrl: String,
    val situationList: ArrayList<String>,
    val partnershipInfo: String,
    val evaluationCount: Int,
    val restaurantScore: Double,
    val isEvaluated: Boolean,
    val isFavorite: Boolean,
    val favoriteCount: Int,
    val restaurantMenuList: List<DetailRestaurantMenu>,
) {
    companion object {
        fun empty() = DetailRestaurant(
            restaurantId = 0,
            restaurantImgUrl = "",
            mainTier = 0,
            isTempTier = false,
            restaurantCuisine = "",
            restaurantCuisineImgUrl = "",
            restaurantPosition = "",
            restaurantName = "",
            restaurantAddress = "",
            isOpen = false,
            businessHours = "",
            naverMapUrl = "",
            situationList = arrayListOf(),
            partnershipInfo = "",
            evaluationCount = 0,
            restaurantScore = 0.0,
            isEvaluated = false,
            isFavorite = false,
            favoriteCount = 0,
            restaurantMenuList = emptyList(),
        )
    }
}

data class DetailRestaurantMenu(
    val menuId: Int,
    val restaurantId: Int,
    val menuName: String,
    val menuPrice: String,
    val naverType: String,
    val menuImgUrl: String,
)

data class DetailReview(
    val evalId: Int,
    val evalScore: Double,
    val writerIconImgUrl: String,
    val writerNickname: String,
    val timeAgo: String,
    val evalImgUrl: String,
    val evalBody: String,
    val reactionType: String,
    val evalLikeCount: Int,
    val evalDislikeCount: Int,
    val isEvaluationMine: Boolean,
    val evalCommentList: List<DetailReviewComment>,
)

data class DetailReviewComment(
    val commentId: Int,
    val writerIconImgUrl: String,
    val writerNickname: String,
    val timeAgo: String,
    val commentBody: String,
    val reactionType: String,
    val commentLikeCount: Int,
    val commentDislikeCount: Int,
    val isCommentMine: Boolean,
)
