package com.kus.appkit.di

import com.kus.appkit.community.write.IosCommunityEditorRenderer
import com.kus.appkit.community.write.image.IosImagePickerFactory
import com.kus.appkit.community.write.image.IosPlatformImageResolver
import com.kus.data.community.PlatformImageResolver
import com.kus.feature.community.ui.write.CommunityEditorRenderer
import com.kus.feature.community.ui.write.image.PlatformImagePickerFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val iosCommunityModule = module {
    singleOf(::IosCommunityEditorRenderer) bind CommunityEditorRenderer::class
    singleOf(::IosPlatformImageResolver) bind PlatformImageResolver::class
    singleOf(::IosImagePickerFactory) bind PlatformImagePickerFactory::class
}