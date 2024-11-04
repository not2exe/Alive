package ru.notexe.alive.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

internal fun NavController.navigateWithLifecycleState(
    route: Any,
    backStackEntry: NavBackStackEntry,
) {
    if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
        navigate(route)
    }
}