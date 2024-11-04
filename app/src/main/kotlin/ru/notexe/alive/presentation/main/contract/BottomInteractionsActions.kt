package ru.notexe.alive.presentation.main.contract

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
internal interface BottomInteractionsActions {
    fun onPaintingModeChanged(paintingMode: PaintingMode)
    fun onShapeAddClick()
    fun onDropDownDismiss()
    fun onPalletClick()
    fun onColorClick(color: Color)
    fun onColorChangeClick()
}