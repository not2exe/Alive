package ru.notexe.alive.presentation.contract

import androidx.compose.runtime.Immutable

@Immutable
interface BottomInteractionsActions {
    fun onPaintingModeChanged(paintingMode: PaintingMode)
    fun onShapeAddClick()
    fun onColorChanged()
}