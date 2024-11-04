package ru.notexe.alive.presentation.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.notexe.alive.presentation.settings.SettingsScreen

internal fun NavGraphBuilder.registerSettingsScreen(
    navController: NavController,
) {
    composable<SettingsRoute> {
        SettingsScreen()
    }
}