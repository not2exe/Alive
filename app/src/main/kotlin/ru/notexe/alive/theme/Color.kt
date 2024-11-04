package ru.notexe.alive.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

internal object AliveColors {
    val black = Color(0xFF000000)
    val white = Color(0xFFFFFFFF)
    val grey = Color(0xFF555454).copy(alpha = 0.16f)
    val grey2 = Color(0xFF8B8B8B)
    val black2 = Color(0xFF1C1C1C)

    val greenPallet = Pallet(
        dull = Color(0xFFFFFECC),
        light = Color(0xFFF3ED00),
        original = Color(0xFFA8DB10),
        dark = Color(0xFF75BB41),
        veryDark = Color(0xFF4E7A25),
    )

    val pinkPallet = Pallet(
        dull = Color(0xFFFF95D5),
        light = Color(0xFFF8D3E3),
        original = Color(0xFFFB66A4),
        dark = Color(0xFFDC0057),
        veryDark = Color(0xFF9D234C),
    )

    val orangePallet = Pallet(
        dull = Color(0xFFFFD1A9),
        light = Color(0xFFFA9A46),
        original = Color(0xFFFC7600),
        dark = Color(0xFFED746C),
        veryDark = Color(0xFFFF3D00),
    )

    val purplePallet = Pallet(
        dull = Color(0xFFEDCAFF),
        light = Color(0xFFB18CFE),
        original = Color(0xFF9747FF),
        dark = Color(0xFF4D21B2),
        veryDark = Color(0xFF641580),
    )

    val bluePallet = Pallet(
        dull = Color(0xFFCCF3FF),
        light = Color(0xFFCCF3FF),
        original = Color(0xFF00C9FB),
        dark = Color(0xFF73A8FC),
        veryDark = Color(0xFF1976D2)
    )
}

internal data class Pallet(
    val dull: Color,
    val light: Color,
    val original: Color,
    val dark: Color,
    val veryDark: Color,
)

internal val LocalTokens = compositionLocalOf<AliveTokens> {
    error("No tokens were provided")
}

@Immutable
internal data class AliveTokens(
    val background: Color,
    val primary: Color,
    val disabled: Color,
) {
    companion object {

        fun light() = AliveTokens(
            background = AliveColors.white,
            primary = AliveColors.black,
            disabled = AliveColors.grey2,
        )

        fun dark() = AliveTokens(
            background = AliveColors.black,
            primary = AliveColors.white,
            disabled = AliveColors.grey2,
        )
    }
}