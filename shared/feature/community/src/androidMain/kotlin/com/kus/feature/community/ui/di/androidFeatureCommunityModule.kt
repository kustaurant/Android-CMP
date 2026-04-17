package com.kus.feature.community.ui.di

import com.kus.feature.community.ui.write.AndroidCommunityEditorRenderer
import com.kus.feature.community.ui.write.CommunityEditorRenderer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val androidFeatureCommunityModule = module {
    singleOf(::AndroidCommunityEditorRenderer) bind CommunityEditorRenderer::class
}