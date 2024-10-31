package ru.notexe.alive.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

internal object AliveColors {
    val black = Color(0xFF000000)
    val white = Color(0xFFFFFFFF)
    val toxicGreen = Color(0xFFA8DB10)
}

internal val LocalTokens = compositionLocalOf<AliveTokens> {
    error("No tokens were provided")
}

internal data class AliveTokens(
    val background: Color,
    val primary: Color,
) {
    companion object {

        fun light() = AliveTokens(
            background = AliveColors.white,
            primary = AliveColors.black,
        )

        fun dark() = AliveTokens(
            background = AliveColors.black,
            primary = AliveColors.white,
        )
    }
}