package ru.notexe.alive.presentation.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import ru.notexe.alive.navigation.navigateWithLifecycleState
import ru.notexe.alive.presentation.main.AliveMainScreen

internal fun NavGraphBuilder.registerMainScreen(
    navController: NavController,
) {
    composable<MainNavigationRoute> { entry ->
        AliveMainScreen(
            aliveMainViewModel = koinViewModel(),
            onNavigate = { route ->
                navController.navigateWithLifecycleState(
                    route = route,
                    backStackEntry = entry,
                )
            }
        )
    }
}