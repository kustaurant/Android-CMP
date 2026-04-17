package com.kus.appkit.di

import com.kus.appkit.community.write.IosCommunityEditorRenderer
import com.kus.feature.community.ui.write.CommunityEditorRenderer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val iosCommunityModule = module {
    singleOf(::IosCommunityEditorRenderer) bind CommunityEditorRenderer::class
}