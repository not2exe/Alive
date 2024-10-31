package ru.notexe.alive.presentation.contract

import androidx.annotation.DrawableRes
import ru.notexe.alive.R

enum class PaintingMode(
    @DrawableRes
    val iconResource: Int,
) {
    PENCIL(
        iconResource = R.drawable.ic_pencil_32,
    ),
    BRUSH(
        iconResource = R.drawable.ic_brush_32,
    ),
    ERASE(
        iconResource = R.drawable.ic_erase_32,
    ),
    FIGURE(
        iconResource = R.drawable.ic_instruments_32,
    )
}