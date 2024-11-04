package ru.notexe.alive.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.KoinAndroidContext
import ru.notexe.alive.presentation.main.navigation.MainNavigationRoute
import ru.notexe.alive.presentation.main.navigation.registerMainScreen
import ru.notexe.alive.presentation.settings.navigation.registerSettingsScreen
import ru.notexe.alive.theme.AliveTheme

internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AliveTheme {
                KoinAndroidContext {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = MainNavigationRoute,
                        enterTransition = { defaultSlideEnterTransition() },
                        exitTransition = { ExitTransition.None },
                        popExitTransition = { defaultSlidePopExitTransition() },
                        popEnterTransition = { EnterTransition.None },
                    ) {
                        registerMainScreen(
                            navController = navController,
                        )
                        registerSettingsScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    private fun defaultSlideEnterTransition(): EnterTransition = slideInHorizontally(
        animationSpec = tween(),
    ) { screenWidth -> (screenWidth * 0.3).toInt() } +
            fadeIn(animationSpec = tween())

    private fun defaultSlidePopExitTransition(): ExitTransition = slideOutHorizontally(
        animationSpec = tween(),
    ) { screenWidth -> (screenWidth * 0.3).toInt() } + fadeOut(animationSpec = tween())
}