package ru.notexe.alive.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import ru.notexe.alive.domain.ColorProperties
import ru.notexe.alive.domain.Frame
import ru.notexe.alive.domain.Line
import ru.notexe.alive.domain.PaintMode
import ru.notexe.alive.domain.PaintObject
import ru.notexe.alive.presentation.contract.LinePresentation
import ru.notexe.alive.presentation.contract.PaintObjectPresentation
import ru.notexe.alive.presentation.contract.PaintingMode

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


    private fun mapPaintObject(paintObject: PaintObject): PaintObjectPresentation = PaintObjectPresentation(
        lines = paintObject.lines.map(::mapLine).toImmutableList(),
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
        }
    )

    private fun mapLine(line: Line) = LinePresentation(
        start = Offset(
            x = line.startX,
            y = line.startY
        ),
        end = Offset(
            x = line.endX,
            y = line.endY,
        )
    )

    private fun mapPaintObjectToDomain(paintObjectPresentation: PaintObjectPresentation): PaintObject = PaintObject(
        lines = paintObjectPresentation.lines.map(::mapLineToDomain),
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
        }
    )

    private fun mapLineToDomain(line: LinePresentation): Line = Line(
        startX = line.start.x,
        startY = line.start.y,
        endX = line.end.x,
        endY = line.end.y
    )
}