package com.kus.data.community

import com.kus.data.community.model.ResolvedImage

interface PlatformImageResolver {
    suspend fun resolve(imagePath: String): ResolvedImage
}