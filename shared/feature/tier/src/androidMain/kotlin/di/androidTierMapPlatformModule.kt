package di

import com.kus.feature.tier.ui.map.TierMapPlatform
import com.kus.feature.tier.ui.map.TierMapPlatformAndroid
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val androidTierMapPlatformModule = module {
    singleOf(::TierMapPlatformAndroid) bind TierMapPlatform::class
}