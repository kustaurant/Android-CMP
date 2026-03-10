package com.kus.domain.community.model

sealed class UploadImageException(message: String) : Exception(message) {
    class TooLarge(val maxBytes: Long) : UploadImageException(
        "이미지는 1MB 이하만 업로드 가능합니다."
    )

    class ReadFailed : UploadImageException("이미지를 읽을 수 없습니다.")
}