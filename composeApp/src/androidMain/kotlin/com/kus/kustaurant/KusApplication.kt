package com.kus.kustaurant

import android.app.Application
import com.kus.core.config.BuildKonfig
import com.kus.data.firstLaunch.di.androidFirstLaunchModule
import com.kus.kustaurant.di.initKoin
import com.kus.logging.initLogger
import com.naver.maps.map.NaverMapSdk
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.core.data.datastore.NidOAuthInitializingCallback
import org.koin.android.ext.koin.androidContext

class KusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildKonfig.NAVER_MAP_CLIENT_ID)
 
        NidOAuth.initialize(
            context = this,
            clientId = BuildConfig.NAVER_CLIENT_ID,
            clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
            clientName = getString(R.string.app_name),
            callback = object : NidOAuthInitializingCallback {
                override fun onSuccess() {  }
                override fun onFailure(e: Exception) {}
            }
        )

        initKoin(
            config = { androidContext(this@KusApplication) },
            additionalModules = listOf(androidFirstLaunchModule)
        )
    }
}