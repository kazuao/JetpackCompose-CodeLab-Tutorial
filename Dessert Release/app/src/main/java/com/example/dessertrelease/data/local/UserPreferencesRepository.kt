package com.example.dessertrelease.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataSource: DataStore<Preferences>
) {
    val isLinearLayout: Flow<Boolean> = dataSource.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference -> preference[IS_LINEAR_LAYOUT] ?: true }

    suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        dataSource.edit { preference ->
            preference[IS_LINEAR_LAYOUT] = isLinearLayout
        }
    }

    private companion object {
        var IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        const val TAG = "UserPreferencesRepo"
    }
}