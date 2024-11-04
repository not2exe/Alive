package ru.notexe.alive.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

internal object AliveTheme {

    val tokens: AliveTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalTokens.current

    val shapes: Shapes = Shapes()
}

@Composable
internal fun AliveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val tokens = remember {
        if (darkTheme) AliveTokens.dark() else AliveTokens.light()
    }
    val indication = remember {
        ripple(
            color = tokens.primary,
        )
    }

    CompositionLocalProvider(
        LocalTokens provides tokens,
        LocalIndication provides indication,
    ) {
        content()
    }
}