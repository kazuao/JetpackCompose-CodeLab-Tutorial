/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelab.android.datastore.data

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val SORT_ORDER_KEY = "sort_order"

enum class SortOrder {
    NONE,
    BY_DEADLINE,
    BY_PRIORITY,
    BY_DEADLINE_AND_PRIORITY
}

data class UserPreferences(
    val showCompleted: Boolean,
    val sortOrder: SortOrder,
)

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {

    private object PreferencesKeys {
        val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )
            val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false

            UserPreferences(showCompleted, sortOrder)
        }

    suspend fun updateShowCompleted(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
        }
    }

//    private val sharedPreferences =
//        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    // Keep the sort order as a stream of changes
//    private val _sortOrderFlow = MutableStateFlow(sortOrder)
//    val sortOrderFlow: StateFlow<SortOrder> = _sortOrderFlow

    /**
     * Get the sort order. By default, sort order is None.
     */
//    private val sortOrder: SortOrder
//        get() {
//            val order = sharedPreferences.getString(SORT_ORDER_KEY, SortOrder.NONE.name)
//            return SortOrder.valueOf(order ?: SortOrder.NONE.name)
//        }



    suspend fun enableSortByDeadline(enable: Boolean) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )

            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_PRIORITY) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_DEADLINE
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_PRIORITY
                    } else {
                        SortOrder.NONE
                    }
                }
            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
        }
//
//        val currentOrder = sortOrderFlow.value
//        val newSortOrder =
//            if (enable) {
//                if (currentOrder == SortOrder.BY_PRIORITY) {
//                    SortOrder.BY_DEADLINE_AND_PRIORITY
//                } else {
//                    SortOrder.BY_DEADLINE
//                }
//            } else {
//                if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
//                    SortOrder.BY_PRIORITY
//                } else {
//                    SortOrder.NONE
//                }
//            }
//        updateSortOrder(newSortOrder)
//        _sortOrderFlow.value = newSortOrder
    }

    suspend fun enableSortByPriority(enable: Boolean) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )

            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_DEADLINE) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_PRIORITY
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_DEADLINE
                    } else {
                        SortOrder.NONE
                    }
                }
            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
        }

//        val currentOrder = sortOrderFlow.value
//        val newSortOrder =
//            if (enable) {
//                if (currentOrder == SortOrder.BY_DEADLINE) {
//                    SortOrder.BY_DEADLINE_AND_PRIORITY
//                } else {
//                    SortOrder.BY_PRIORITY
//                }
//            } else {
//                if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
//                    SortOrder.BY_DEADLINE
//                } else {
//                    SortOrder.NONE
//                }
//            }
//        updateSortOrder(newSortOrder)
//        _sortOrderFlow.value = newSortOrder
    }

//    private fun updateSortOrder(sortOrder: SortOrder) {
//        sharedPreferences.edit {
//            putString(SORT_ORDER_KEY, sortOrder.name)
//        }
//    }

//    companion object {
//        @Volatile
//        private var INSTANCE: UserPreferencesRepository? = null
//
//        fun getInstance(context: Context): UserPreferencesRepository {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE?.let {
//                    return it
//                }
//
//                val instance = UserPreferencesRepository(context)
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}
