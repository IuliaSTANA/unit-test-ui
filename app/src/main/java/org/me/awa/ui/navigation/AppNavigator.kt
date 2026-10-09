package org.me.awa.ui.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() {
    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(AppNavKey.Favorites)

    fun navigateTo(key: AppNavKey) {
        backStack.add(key)
    }

    fun navigateBack() {
        backStack.removeLastOrNull()
    }

    fun remove(key: AppNavKey) {
        backStack.remove(key)
    }
    fun cleanAndNavigateTo(vararg routes: AppNavKey) {
        withMutableSnapshot {
            backStack.clear()
            backStack.addAll(routes)
        }
    }
}
