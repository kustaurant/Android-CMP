package com.kus.feature.community.model

sealed interface DeleteCommunityEvent {
    data object Deleted : DeleteCommunityEvent
    data class Error(val message: String) : DeleteCommunityEvent
}