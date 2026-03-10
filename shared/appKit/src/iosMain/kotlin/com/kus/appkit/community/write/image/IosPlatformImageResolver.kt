package com.kus.appkit.community.write.image

import com.kus.appkit.community.util.toByteArray
import com.kus.data.community.PlatformImageResolver
import com.kus.data.community.model.ResolvedImage
import com.kus.domain.community.model.UploadImageException
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile

class IosPlatformImageResolver(
    private val maxBytes: Long = 1_048_576L,
): PlatformImageResolver {
    override suspend fun resolve(imagePath: String): ResolvedImage {
        val data = NSData.dataWithContentsOfFile(imagePath)
            ?: throw UploadImageException.ReadFailed()

        if (data.length.toLong() > maxBytes) {
            throw UploadImageException.TooLarge(maxBytes)
        }

        return ResolvedImage(
            bytes = data.toByteArray(),
            mimeType = "image/jpeg",
            fileName = imagePath.substringAfterLast('/')
        )
    }
}