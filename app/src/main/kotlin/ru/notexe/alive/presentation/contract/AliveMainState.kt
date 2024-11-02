package ru.notexe.alive.presentation.contract

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.notexe.alive.R

@Immutable
internal data class AliveMainState(
    val currentPaintingFrame: ImmutableList<PaintObject> = persistentListOf(),
    val redoList: ImmutableList<PaintObject> = persistentListOf(),
    val paintingSettings: PaintingSettings = PaintingSettings(),
    val currentAnimationFrame: ImmutableList<PaintObject>? = null,
    val playEnabled: Boolean = false,
    val smallDropDownState: SmallDropDownState? = null,
    val bigDropDown: BigDropDownState? = null,
) {
    val redoEnabled: Boolean
        get() = redoList.isNotEmpty()

    val undoEnabled: Boolean
        get() = currentPaintingFrame.isNotEmpty()

    val pauseEnabled: Boolean
        get() = currentAnimationFrame != null
}

@Immutable
internal data class PaintingSettings(
    val paintingMode: PaintingMode = PaintingMode.PENCIL,
    val strokeWidth: Dp = 6.dp,
    val currentColor: Color = Color.Black,
)

@Immutable
internal enum class PaintingMode(
    @DrawableRes
    val iconResource: Int,
    val strokeWidthRange: IntRange,
    val strokeCap: StrokeCap,
    val blendMode: BlendMode,
) {
    PENCIL(
        iconResource = R.drawable.ic_pencil_32,
        strokeWidthRange = 1..10,
        strokeCap = StrokeCap.Square,
        blendMode = BlendMode.SrcOver,
    ),
    BRUSH(
        iconResource = R.drawable.ic_brush_32,
        strokeWidthRange = 5..50,
        strokeCap = StrokeCap.Round,
        blendMode = BlendMode.SrcOver,
    ),
    ERASE(
        iconResource = R.drawable.ic_erase_32,
        strokeWidthRange = 1..50,
        strokeCap = StrokeCap.Round,
        blendMode = BlendMode.Clear,
    ),
    FIGURE(
        iconResource = R.drawable.ic_instruments_32,
        strokeWidthRange = 2..2,
        strokeCap = StrokeCap.Round,
        blendMode = BlendMode.SrcOver,
    )
}

@Immutable
internal data class PaintObject(
    val lines: ImmutableList<Line>,
    val color: Color,
    val strokeWidth: Dp,
    val paintingMode: PaintingMode,
)


@Immutable
internal data class Line(
    val start: Offset,
    val end: Offset,
)

internal enum class SmallDropDownState {
    FIGURE,
    SMALL_PALLET
}

internal enum class BigDropDownState {
    BIG_PALLET,
}