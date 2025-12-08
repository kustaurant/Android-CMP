package com.kus.data.firstLaunch.local

import com.kus.data.firstLaunch.constants.KEY_FIRST_LAUNCH
import com.kus.data.firstLaunch.dataSource.FirstLaunchLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSUserDefaults


class IosFirstLaunchLocalDataSource(
    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()
) : FirstLaunchLocalDataSource {

    private val _isFirstLaunchFlow = MutableStateFlow(readFromDefaults())

    override val isFirstLaunchFlow: Flow<Boolean> = _isFirstLaunchFlow

    private fun readFromDefaults(): Boolean {
        val obj = defaults.objectForKey(KEY_FIRST_LAUNCH)
        return if (obj == null) {
            true
        } else {
            defaults.boolForKey(KEY_FIRST_LAUNCH)
        }
    }

    override suspend fun setOnboardingSeen() {
        defaults.setBool(false, forKey = KEY_FIRST_LAUNCH)
        defaults.synchronize()
        _isFirstLaunchFlow.value = false
    }
}