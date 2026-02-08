package com.kus.feature.detail.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _reviewUiState = MutableStateFlow(DetailReviewUiState())
    val reviewUiState: StateFlow<DetailReviewUiState> = _reviewUiState.asStateFlow()

    private val dummyReviewList = listOf(
        DetailReview(
            evalId = 1,
            evalScore = 4.5,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer1/80/80",
            writerNickname = "역병",
            timeAgo = "2분전",
            evalImgUrl = "https://picsum.photos/seed/review1/300/200",
            evalBody = "오 좀 맛있는데?",
            reactionType = "LIKE",
            evalLikeCount = 14,
            evalDislikeCount = 3,
            isEvaluationMine = true,
            evalCommentList = listOf(
                DetailReviewComment(
                    commentId = 1,
                    writerIconImgUrl = "https://picsum.photos/seed/commenter1/60/60",
                    writerNickname = "역병",
                    timeAgo = "2분전",
                    commentBody = "저두 이 의견에 동의합니당",
                    reactionType = "LIKE",
                    commentLikeCount = 14,
                    commentDislikeCount = 3,
                    isCommentMine = true,
                )
            )
        ),
        DetailReview(
            evalId = 2,
            evalScore = 4.0,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer2/80/80",
            writerNickname = "칼국수러버",
            timeAgo = "5분전",
            evalImgUrl = "https://picsum.photos/seed/review2/300/200",
            evalBody = "국물이 깔끔했어요.",
            reactionType = "LIKE",
            evalLikeCount = 9,
            evalDislikeCount = 1,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 3,
            evalScore = 3.5,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer3/80/80",
            writerNickname = "면덕후",
            timeAgo = "10분전",
            evalImgUrl = "https://picsum.photos/seed/review3/300/200",
            evalBody = "면이 쫄깃해서 좋았어요.",
            reactionType = "NONE",
            evalLikeCount = 4,
            evalDislikeCount = 0,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 4,
            evalScore = 5.0,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer4/80/80",
            writerNickname = "단골손님",
            timeAgo = "20분전",
            evalImgUrl = "https://picsum.photos/seed/review4/300/200",
            evalBody = "여긴 진짜 찐맛집!",
            reactionType = "LIKE",
            evalLikeCount = 32,
            evalDislikeCount = 2,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 5,
            evalScore = 4.2,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer5/80/80",
            writerNickname = "맛집탐방",
            timeAgo = "30분전",
            evalImgUrl = "https://picsum.photos/seed/review5/300/200",
            evalBody = "가격 대비 만족!",
            reactionType = "LIKE",
            evalLikeCount = 11,
            evalDislikeCount = 1,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 6,
            evalScore = 3.8,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer6/80/80",
            writerNickname = "배고파",
            timeAgo = "1시간전",
            evalImgUrl = "https://picsum.photos/seed/review6/300/200",
            evalBody = "양이 많아서 좋아요.",
            reactionType = "NONE",
            evalLikeCount = 6,
            evalDislikeCount = 1,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 7,
            evalScore = 4.7,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer7/80/80",
            writerNickname = "리뷰왕",
            timeAgo = "2시간전",
            evalImgUrl = "https://picsum.photos/seed/review7/300/200",
            evalBody = "재방문 의사 100%",
            reactionType = "LIKE",
            evalLikeCount = 21,
            evalDislikeCount = 0,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 8,
            evalScore = 3.2,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer8/80/80",
            writerNickname = "솔직후기",
            timeAgo = "3시간전",
            evalImgUrl = "https://picsum.photos/seed/review8/300/200",
            evalBody = "살짝 짰어요.",
            reactionType = "DISLIKE",
            evalLikeCount = 2,
            evalDislikeCount = 3,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 9,
            evalScore = 4.1,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer9/80/80",
            writerNickname = "맛알못",
            timeAgo = "4시간전",
            evalImgUrl = "https://picsum.photos/seed/review9/300/200",
            evalBody = "무난하게 맛있어요.",
            reactionType = "LIKE",
            evalLikeCount = 7,
            evalDislikeCount = 1,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
        DetailReview(
            evalId = 10,
            evalScore = 4.9,
            writerIconImgUrl = "https://picsum.photos/seed/reviewer10/80/80",
            writerNickname = "최고최고",
            timeAgo = "1일전",
            evalImgUrl = "https://picsum.photos/seed/review10/300/200",
            evalBody = "여기만 오면 행복",
            reactionType = "LIKE",
            evalLikeCount = 40,
            evalDislikeCount = 0,
            isEvaluationMine = false,
            evalCommentList = emptyList()
        ),
    )

    init {
        _uiState.value = DetailUiState(
            restaurant = DetailRestaurant(
                restaurantId = 1,
                restaurantImgUrl = "https://search.pstatic.net/common/?autoRotate=true&type=w560_sharpen&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221219_73%2F1671415873694AWTMq_JPEG%2FDSC04440.jpg",
                mainTier = 1,
                isTempTier = false,
                restaurantCuisine = "한식",
                restaurantCuisineImgUrl = "https://kustaurant.com/img/home/%EA%B3%A0%EA%B8%B0.png",
                restaurantPosition = "건입~중문",
                restaurantName = "제주곤이칼국수 건대점",
                restaurantAddress = "서울시 광진구 어딘가 222-22, 304호",
                isOpen = true,
                businessHours = "오늘 10:00~20:00",
                naverMapUrl = "https://map.naver.com/p/entry/place/12785886?c=20.00,0,0,0,dh",
                situationList = arrayListOf("혼밥", "배달"),
                partnershipInfo = "학생증 제시 시에 전메뉴 10% 할인 대박!!!! 학생증 제시 시에 전메뉴 10% 할인 대박!!!! 학생증 제시 시에 전메뉴 10% 할인 대박!!!! 학생증 제시 시에 전메뉴 10% 할인 대박!!!!",
                evaluationCount = 100,
                restaurantScore = 4.4,
                isEvaluated = false,
                isFavorite = false,
                favoriteCount = 0,
                restaurantMenuList = listOf(
                    DetailRestaurantMenu(
                        menuId = 1,
                        restaurantId = 1,
                        menuName = "곤이칼국수",
                        menuPrice = "9,000원",
                        naverType = "대표",
                        menuImgUrl = "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230703_3%2F1688371416401iXlcd_JPEG%2FEmmoBollP-9f7S9t1Tm8Ia0MCo4L7avZscDoUWphehEnIczhsCfxtBcVpWjFku8X.jpg&quot",
                    ),
                    DetailRestaurantMenu(
                        menuId = 2,
                        restaurantId = 1,
                        menuName = "얼큰칼국수",
                        menuPrice = "9,500원",
                        naverType = "추천",
                        menuImgUrl = "https://picsum.photos/seed/menu2/200/200",
                    ),
                    DetailRestaurantMenu(
                        menuId = 3,
                        restaurantId = 1,
                        menuName = "해물파전",
                        menuPrice = "12,000원",
                        naverType = "사이드",
                        menuImgUrl = "https://picsum.photos/seed/menu3/200/200",
                    ),
                    DetailRestaurantMenu(
                        menuId = 4,
                        restaurantId = 1,
                        menuName = "왕만두",
                        menuPrice = "6,000원",
                        naverType = "사이드",
                        menuImgUrl = "https://picsum.photos/seed/menu4/200/200",
                    ),
                    DetailRestaurantMenu(
                        menuId = 5,
                        restaurantId = 1,
                        menuName = "수제비",
                        menuPrice = "8,500원",
                        naverType = "메인",
                        menuImgUrl = "https://picsum.photos/seed/menu5/200/200",
                    ),
                    DetailRestaurantMenu(
                        menuId = 6,
                        restaurantId = 1,
                        menuName = "비빔국수",
                        menuPrice = "8,000원",
                        naverType = "메인",
                        menuImgUrl = "https://picsum.photos/seed/menu6/200/200",
                    ),
                    DetailRestaurantMenu(
                        menuId = 7,
                        restaurantId = 1,
                        menuName = "볶음밥",
                        menuPrice = "7,000원",
                        naverType = "사이드",
                        menuImgUrl = "https://picsum.photos/seed/menu7/200/200",
                    ),
                    DetailRestaurantMenu(
                        menuId = 8,
                        restaurantId = 1,
                        menuName = "김치전",
                        menuPrice = "10,000원",
                        naverType = "사이드",
                        menuImgUrl = "https://picsum.photos/seed/menu8/200/200",
                    ),
                    DetailRestaurantMenu(
                        menuId = 9,
                        restaurantId = 1,
                        menuName = "공기밥",
                        menuPrice = "1,000원",
                        naverType = "기타",
                        menuImgUrl = "https://picsum.photos/seed/menu9/200/200",
                    ),
                    DetailRestaurantMenu(
                        menuId = 10,
                        restaurantId = 1,
                        menuName = "음료",
                        menuPrice = "2,000원",
                        naverType = "기타",
                        menuImgUrl = "https://picsum.photos/seed/menu10/200/200",
                    ),
                )
            )
        )
    }

    fun loadReviewsIfNeeded() {
        if (_reviewUiState.value.hasLoaded || _reviewUiState.value.isLoading) return
        loadReviews(_reviewUiState.value.sort)
    }

    fun loadReviews(sort: ReviewSort) {
        _reviewUiState.value = _reviewUiState.value.copy(isLoading = true, sort = sort)
        val list = when (sort) {
            ReviewSort.Popular -> dummyReviewList
            ReviewSort.Latest -> dummyReviewList.reversed()
        }
        _reviewUiState.value = DetailReviewUiState(
            reviewList = list,
            isLoading = false,
            hasLoaded = true,
            sort = sort,
        )
    }
}
