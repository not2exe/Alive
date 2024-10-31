package ru.notexe.alive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.geometry.Offset
import ru.notexe.alive.presentation.AliveMainScreen
import ru.notexe.alive.ui.theme.AliveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AliveTheme {
                AliveMainScreen()
            }
        }
    }
}
