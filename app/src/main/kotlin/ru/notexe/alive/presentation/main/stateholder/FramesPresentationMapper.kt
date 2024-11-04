package ru.notexe.alive.presentation.main.stateholder

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import ru.notexe.alive.domain.ColorProperties
import ru.notexe.alive.domain.Frame
import ru.notexe.alive.domain.Change
import ru.notexe.alive.domain.PaintMode
import ru.notexe.alive.domain.PaintObject
import ru.notexe.alive.presentation.main.contract.FramePresentation
import ru.notexe.alive.presentation.main.contract.ChangePresentation
import ru.notexe.alive.presentation.main.contract.PaintObjectPresentation
import ru.notexe.alive.presentation.main.contract.PaintingMode

internal class FramesPresentationMapper {

    fun mapPaintObjectsToDomainFrame(framePresentation: FramePresentation) = Frame(
        id = framePresentation.id,
        paintObjects = framePresentation.paintObjects.map(::mapPaintObjectToDomain),
    )

    fun mapFrames(frames: List<Frame>): List<FramePresentation> = frames.map(::mapFrame)

    fun mapFrame(frame: Frame): FramePresentation = FramePresentation(
        id = frame.id,
        paintObjects = frame
            .paintObjects
            .map(::mapPaintObject)
            .toImmutableList()
    )


    private fun mapPaintObject(paintObject: PaintObject): PaintObjectPresentation {
        val changes = paintObject.changes.map(::mapChange)
        return PaintObjectPresentation(
            path = Path().apply {
                moveTo(paintObject.startX, paintObject.startY)
                changes.forEach { change ->
                    relativeLineTo(change.dragAmount.x, change.dragAmount.y)
                }
            },
            changes = changes.toImmutableList(),
            color = paintObject.colorProperties.run {
                Color(
                    red = red,
                    green = green,
                    blue = blue,
                    alpha = alpha
                )
            },
            strokeWidth = paintObject.lineStrokeWidth.dp,
            paintingMode = when (paintObject.paintingMode) {
                PaintMode.PENCIL -> PaintingMode.PENCIL
                PaintMode.BRUSH -> PaintingMode.BRUSH
                PaintMode.ERASE -> PaintingMode.ERASE
                PaintMode.SHAPE -> PaintingMode.FIGURE
            },
            start = Offset(
                x = paintObject.startX,
                y = paintObject.startY,
            )
        )
    }

    private fun mapChange(change: Change) = ChangePresentation(
        dragAmount = Offset(
            x = change.dragAmountX,
            y = change.dragAmountY,
        )
    )

    private fun mapPaintObjectToDomain(paintObjectPresentation: PaintObjectPresentation): PaintObject = PaintObject(
        changes = paintObjectPresentation.changes.map(::mapChangeToDomain),
        colorProperties = paintObjectPresentation.color.run {
            ColorProperties(
                red = red,
                green = green,
                blue = blue,
                alpha = alpha
            )
        },
        lineStrokeWidth = paintObjectPresentation.strokeWidth.value,
        paintingMode = when (paintObjectPresentation.paintingMode) {
            PaintingMode.PENCIL -> PaintMode.PENCIL
            PaintingMode.BRUSH -> PaintMode.BRUSH
            PaintingMode.ERASE -> PaintMode.ERASE
            PaintingMode.FIGURE -> PaintMode.SHAPE
        },
        startY = paintObjectPresentation.start.y,
        startX = paintObjectPresentation.start.x,
    )

    private fun mapChangeToDomain(line: ChangePresentation): Change = Change(
        dragAmountX = line.dragAmount.x,
        dragAmountY = line.dragAmount.y,
    )
}