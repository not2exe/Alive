package ru.notexe.alive.presentation.widgets

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

internal class DropDownMenuPositionProvider(
    private val offset: IntOffset,
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val anchorStartX = if (layoutDirection == LayoutDirection.Ltr) anchorBounds.left else anchorBounds.right
        val x = (
                (anchorStartX + anchorBounds.width / 2)
                        - popupContentSize.width / 2
                        + offset.x
                ).coerceIn(0, windowSize.width - popupContentSize.width)
        val y = (anchorBounds.top + offset.y - popupContentSize.height).coerceIn(0, windowSize.height - popupContentSize.height)

        return IntOffset(
            x = x,
            y = y
        )
    }
}