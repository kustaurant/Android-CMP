package com.kus.kustaurant.detail.remote.mapper

import com.kus.kustaurant.detail.remote.response.CommentReactionResponse
import com.kus.kustaurant.detail.remote.response.DetailResponse
import com.kus.kustaurant.detail.remote.response.EvaluationReactionResponse
import com.kus.kustaurant.detail.remote.response.FavoriteResponse
import com.kus.kustaurant.detail.remote.response.MenuResponse
import com.kus.kustaurant.detail.remote.response.ReviewCommentResponse
import com.kus.kustaurant.detail.remote.response.ReviewResponse
import com.kus.shared.domain.model.detail.FavoriteResult
import com.kus.shared.domain.model.detail.ReactionResult
import com.kus.shared.domain.model.detail.RestaurantDetail
import com.kus.shared.domain.model.detail.RestaurantMenu
import com.kus.shared.domain.model.detail.RestaurantReview
import com.kus.shared.domain.model.detail.ReviewComment

fun DetailResponse.toDomain(): RestaurantDetail =
    RestaurantDetail(
        restaurantId = restaurantId,
        restaurantImgUrl = restaurantImgUrl,
        mainTier = mainTier,
        isTempTier = isTempTier,
        restaurantCuisine = restaurantCuisine,
        restaurantCuisineImgUrl = restaurantCuisineImgUrl,
        restaurantPosition = restaurantPosition,
        restaurantName = restaurantName,
        restaurantAddress = restaurantAddress,
        isOpen = isOpen,
        businessHours = businessHours,
        naverMapUrl = naverMapUrl,
        situationList = situationList ?: emptyList(),
        partnershipInfo = partnershipInfo ?: "",
        evaluationCount = evaluationCount,
        restaurantScore = restaurantScore,
        isEvaluated = isEvaluated,
        isFavorite = isFavorite,
        favoriteCount = favoriteCount,
        restaurantMenuList = restaurantMenuList?.map { it.toDomain() } ?: emptyList(),
    )

fun MenuResponse.toDomain(): RestaurantMenu =
    RestaurantMenu(
        menuId = menuId,
        restaurantId = restaurantId,
        menuName = menuName,
        menuPrice = menuPrice,
        naverType = naverType,
        menuImgUrl = menuImgUrl
    )

fun ReviewResponse.toDomain(): RestaurantReview =
    RestaurantReview(
        evalId = evalId,
        evalScore = evalScore,
        writerIconImgUrl = writerIconImgUrl ?: "",
        writerNickname = writerNickname,
        timeAgo = timeAgo,
        evalImgUrl = evalImgUrl ?: "",
        evalBody = evalBody ?: "",
        reactionType = reactionType ?: "",
        evalLikeCount = evalLikeCount,
        evalDislikeCount = evalDislikeCount,
        isEvaluationMine = isEvaluationMine,
        evalCommentList = evalCommentList?.map { it.toDomain() } ?: emptyList(),
    )

fun ReviewCommentResponse.toDomain(): ReviewComment =
    ReviewComment(
        commentId = commentId,
        writerIconImgUrl = writerIconImgUrl ?: "",
        writerNickname = writerNickname,
        timeAgo = timeAgo,
        commentBody = commentBody ?: "",
        reactionType = reactionType ?: "",
        commentLikeCount = commentLikeCount,
        commentDislikeCount = commentDislikeCount,
        isCommentMine = isCommentMine,
    )

fun EvaluationReactionResponse.toDomain(): ReactionResult =
    ReactionResult(
        id = evaluationId,
        reaction = reaction ?: "",
        likeCount = likeCount,
        dislikeCount = dislikeCount,
    )

fun CommentReactionResponse.toDomain(): ReactionResult =
    ReactionResult(
        id = evalCommentId,
        reaction = reaction ?: "",
        likeCount = likeCount,
        dislikeCount = dislikeCount,
    )

fun FavoriteResponse.toDomain(): FavoriteResult =
    FavoriteResult(
        isFavorite = isFavorite,
        favoriteCount = count.coerceAtMost(Int.MAX_VALUE.toLong()).toInt(),
    )
