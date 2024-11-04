package ru.notexe.alive.data

import ru.notexe.alive.data.models.ColorPropertiesDto
import ru.notexe.alive.data.models.FramesResponse
import ru.notexe.alive.data.models.ChangeDto
import ru.notexe.alive.data.models.PaintModeDto
import ru.notexe.alive.data.models.PaintObjectDto
import ru.notexe.alive.domain.ColorProperties
import ru.notexe.alive.domain.Frame
import ru.notexe.alive.domain.Change
import ru.notexe.alive.domain.PaintMode
import ru.notexe.alive.domain.PaintObject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class FramesMapper {

    fun map(framesResponses: List<FramesResponse>): List<Frame> = framesResponses.map(::mapFrameResponse)

    fun mapPaintObjectsToDto(
        list: List<PaintObject>,
        frameId: Long,
    ) = list.map { paintObject -> mapPaintObjectToDto(paintObject, frameId) }

    private fun mapFrameResponse(framesResponse: FramesResponse): Frame = Frame(
        id = framesResponse.frame.id,
        paintObjects = framesResponse.paintObject.map(::mapPaintObject)
    )

    private fun mapPaintObject(paintObject: PaintObjectDto): PaintObject {
        return PaintObject(
            changes = paintObject.changes.map(::mapChange),
            colorProperties = paintObject.colorProperties.run {
                ColorProperties(
                    red = red,
                    green = green,
                    blue = blue,
                    alpha = alpha
                )
            },
            lineStrokeWidth = paintObject.strokeWidth,
            paintingMode = mapPaintMode(paintObject.paintMode),
            startX = paintObject.startX,
            startY = paintObject.startY,
        )
    }

    private fun mapChange(changeDto: ChangeDto): Change = Change(
        dragAmountX = changeDto.dragAmountX,
        dragAmountY = changeDto.dragAmountY,
    )

    private fun mapPaintMode(paintModeDto: PaintModeDto) = when (paintModeDto) {
        PaintModeDto.BRUSH -> PaintMode.BRUSH
        PaintModeDto.ERASE -> PaintMode.ERASE
        PaintModeDto.SHAPE -> PaintMode.SHAPE
        PaintModeDto.PENCIL -> PaintMode.PENCIL
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun mapPaintObjectToDto(
        paintObject: PaintObject,
        frameId: Long,
    ): PaintObjectDto = PaintObjectDto(
        id = Uuid.random().toString(),
        frameId = frameId,
        startX = paintObject.startX,
        startY = paintObject.startY,
        changes = paintObject.changes.map { change ->
            ChangeDto(
                dragAmountX = change.dragAmountX,
                dragAmountY = change.dragAmountY,
            )
        },
        colorProperties = paintObject.colorProperties.run {
            ColorPropertiesDto(
                red = red,
                green = green,
                blue = blue,
                alpha = alpha,
            )
        },
        paintMode = when (paintObject.paintingMode) {
            PaintMode.BRUSH -> PaintModeDto.BRUSH
            PaintMode.ERASE -> PaintModeDto.ERASE
            PaintMode.SHAPE -> PaintModeDto.SHAPE
            PaintMode.PENCIL -> PaintModeDto.PENCIL
        },
        strokeWidth = paintObject.lineStrokeWidth,
    )
}