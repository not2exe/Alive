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
internal data class PaintObjectDto(
    @PrimaryKey
    val id: String,
    @ColumnInfo(
        name = "frame_id",
        index = true
    )
    val frameId: Long,
    @ColumnInfo("start_x")
    val startX: Float,
    @ColumnInfo("start_y")
    val startY: Float,
    @ColumnInfo("changes")
    val changes: List<ChangeDto>,
    @Embedded
    val colorProperties: ColorPropertiesDto,
    @ColumnInfo("paint_mode")
    val paintMode: PaintModeDto,
    @ColumnInfo("stroke_width")
    val strokeWidth: Float,
)