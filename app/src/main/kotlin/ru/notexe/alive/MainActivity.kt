package ru.notexe.alive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import ru.notexe.alive.presentation.AliveMainScreen
import ru.notexe.alive.ui.theme.AliveTheme

internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AliveTheme {
                KoinAndroidContext {
                    AliveMainScreen(
                        aliveMainViewModel = koinViewModel(),
                    )
                }
            }
        }
    }
}