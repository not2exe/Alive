package ru.notexe.alive.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.notexe.alive.data.converters.LineListConverters
import ru.notexe.alive.data.models.FrameDto
import ru.notexe.alive.data.models.PaintObjectDto


@Database(
    entities = [FrameDto::class, PaintObjectDto::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(LineListConverters::class)
internal abstract class FramesDatabase : RoomDatabase() {
    abstract val dao: FramesDao

    companion object {
        const val FRAMES_DB = "frames_db"
        const val FRAME_TABLE = "frames"
        const val PAINT_OBJECTS_TABLE = "paint_objects"
    }
}