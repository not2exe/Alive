package ru.notexe.alive.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.notexe.alive.data.FramesDatabase

@Entity(
    tableName = FramesDatabase.FRAME_TABLE,
)
internal class FrameDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long
)