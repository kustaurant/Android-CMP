package com.kus.feature.community.ui.di

import com.kus.feature.community.ui.write.AndroidCommunityEditorRenderer
import com.kus.feature.community.ui.write.CommunityEditorRenderer
import com.kus.feature.community.ui.write.image.AndroidImagePickerFactory
import com.kus.feature.community.ui.write.image.PlatformImagePickerFactory
import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

val androidFeatureCommunityModule = module {
    singleOf(::AndroidCommunityEditorRenderer) bind CommunityEditorRenderer::class
    singleOf(::AndroidImagePickerFactory) bind PlatformImagePickerFactory::class
}