package ru.notexe.alive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import ru.notexe.alive.presentation.AliveMainScreen
import ru.notexe.alive.presentation.stateholder.AliveMainViewModel
import ru.notexe.alive.ui.theme.AliveTheme

internal class MainActivity : ComponentActivity() {
    private val aliveMainViewModel by viewModels<AliveMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AliveTheme {
                AliveMainScreen(
                    aliveMainViewModel = aliveMainViewModel,
                )
            }
        }
    }
}
