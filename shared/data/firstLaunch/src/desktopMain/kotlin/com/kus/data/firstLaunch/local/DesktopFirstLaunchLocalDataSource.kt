package com.kus.data.firstLaunch.local

import com.kus.data.firstLaunch.dataSource.FirstLaunchLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.prefs.Preferences

private const val APP_PREFS_NODE = "com.kus.kustaurant"
private const val FIRST_LAUNCH_NODE = "first_launch"
private const val KEY_FIRST_LAUNCH = "KEY_FIRST_LAUNCH"

class DesktopFirstLaunchLocalDataSource(
    private val prefs: Preferences =
        Preferences.userRoot()
            .node(APP_PREFS_NODE)
            .node(FIRST_LAUNCH_NODE)
) : FirstLaunchLocalDataSource {

    private val _isFirstLaunchFlow = MutableStateFlow(readFromPrefs())
    override val isFirstLaunchFlow: Flow<Boolean> = _isFirstLaunchFlow

    private fun readFromPrefs(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    override suspend fun setOnboardingSeen() {
        prefs.putBoolean(KEY_FIRST_LAUNCH, false)
        prefs.flush()
        _isFirstLaunchFlow.value = false
    }

    suspend fun resetFirstLaunchForDebug() {
        prefs.putBoolean(KEY_FIRST_LAUNCH, true)
        prefs.flush()
        _isFirstLaunchFlow.value = true
    }

    suspend fun clearFirstLaunchForDebug() {
        prefs.remove(KEY_FIRST_LAUNCH)
        prefs.flush()
        _isFirstLaunchFlow.value = true
    }
}
