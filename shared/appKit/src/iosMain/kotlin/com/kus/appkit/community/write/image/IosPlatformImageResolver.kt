package com.kus.appkit.community.write.image

import com.kus.appkit.community.util.toByteArray
import com.kus.data.community.PlatformImageResolver
import com.kus.data.community.model.ResolvedImage
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile

class IosPlatformImageResolver : PlatformImageResolver {
    override suspend fun resolve(imagePath: String): ResolvedImage {
        val data = NSData.dataWithContentsOfFile(imagePath)
            ?: error("이미지를 읽을 수 없습니다.")

        if (data.length > (10 * 1024 * 1024).toUInt()) {
            error("이미지는 10MB 이하만 업로드 가능합니다.")
        }

        return ResolvedImage(
            bytes = data.toByteArray(),
            mimeType = "image/jpeg",
            fileName = imagePath.substringAfterLast('/')
        )
    }
}