package com.kus.data.firstLaunch.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.kus.data.firstLaunch.constants.APP_LAUNCH_PREFS
import com.kus.data.firstLaunch.constants.PREF_FIRST_LAUNCH
import com.kus.data.firstLaunch.dataSource.FirstLaunchLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException 
private val Context.appLaunchDataStore by preferencesDataStore(APP_LAUNCH_PREFS)
private val KEY_FIRST_LAUNCH = booleanPreferencesKey(PREF_FIRST_LAUNCH)

class AndroidFirstLaunchLocalDataSource(
    private val context: Context
) : FirstLaunchLocalDataSource {

    override val isFirstLaunchFlow: Flow<Boolean> =
        context.appLaunchDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs ->
                prefs[KEY_FIRST_LAUNCH] ?: true
            }

    override suspend fun setOnboardingSeen() {
        context.appLaunchDataStore.edit { prefs ->
            prefs[KEY_FIRST_LAUNCH] = false
        }
    }
}