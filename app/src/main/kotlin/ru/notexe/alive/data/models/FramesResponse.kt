package ru.notexe.alive.data.models

import androidx.room.Embedded
import androidx.room.Relation

internal class FramesResponse(
    @Embedded
    val frame: FrameDto,
    @Relation(
        parentColumn = "id",
        entityColumn = "frame_id"
    )
    val paintObject: List<PaintObjectDto>
)