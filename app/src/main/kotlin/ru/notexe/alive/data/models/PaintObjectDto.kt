package ru.notexe.alive.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.notexe.alive.data.FramesDatabase

@Entity(
    tableName = FramesDatabase.PAINT_OBJECTS_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = FrameDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("frame_id"),
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = []
)
internal class PaintObjectDto(
    @PrimaryKey
    val id: String,
    @ColumnInfo(
        name = "frame_id",
        index = true
    )
    val frameId: Long,
    @ColumnInfo("lines")
    val lines: List<LineDto>,
    @Embedded
    val colorProperties: ColorPropertiesDto,
    @ColumnInfo("paint_mode")
    val paintMode: PaintModeDto,
    @ColumnInfo("stroke_width")
    val strokeWidth: Float,
)