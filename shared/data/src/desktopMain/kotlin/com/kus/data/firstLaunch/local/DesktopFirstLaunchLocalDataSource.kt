package com.kus.data.firstLaunch.local

import com.kus.data.firstLaunch.constants.KEY_FIRST_LAUNCH
import com.kus.data.firstLaunch.constants.PREF_FIRST_LAUNCH
import com.kus.data.firstLaunch.dataSource.FirstLaunchLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.prefs.Preferences

class DesktopFirstLaunchLocalDataSource(
    private val prefs: Preferences = Preferences.userRoot().node(PREF_FIRST_LAUNCH)
) : FirstLaunchLocalDataSource {

    private val _isFirstLaunchFlow = MutableStateFlow(readFromPrefs())

    override val isFirstLaunchFlow: Flow<Boolean> = _isFirstLaunchFlow

    private fun readFromPrefs(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, /* def */ true)
    }

    override suspend fun setOnboardingSeen() {
        prefs.putBoolean(KEY_FIRST_LAUNCH, false)
        _isFirstLaunchFlow.value = false
    }
}