package ru.notexe.alive.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.notexe.alive.data.models.FrameDto
import ru.notexe.alive.data.models.FramesResponse
import ru.notexe.alive.data.models.PaintObjectDto

@Dao
internal interface FramesDao {

    @Transaction
    @Query("SELECT * FROM ${FramesDatabase.FRAME_TABLE} WHERE id BETWEEN :from AND :last")
    suspend fun getFrames(
        from: Long,
        last: Long,
    ): List<FramesResponse>

    @Query("SELECT COUNT(*) FROM ${FramesDatabase.FRAME_TABLE}")
    fun count(): Flow<Long>

    @Query("SELECT id FROM ${FramesDatabase.FRAME_TABLE} ORDER BY ID DESC LIMIT 1")
    suspend fun lastId(): Long

    @Insert
    suspend fun addFrame(frameDto: FrameDto = FrameDto(0)): Long

    @Insert
    suspend fun insertPaintObjects(paintObject: List<PaintObjectDto>): List<Long>

    @Query("DELETE FROM ${FramesDatabase.FRAME_TABLE} WHERE id = :id")
    suspend fun deleteFrameById(id: Long): Int

    @Query("DELETE FROM ${FramesDatabase.PAINT_OBJECTS_TABLE} WHERE frame_id =:frameId")
    suspend fun deletePaintObjectsById(frameId: Long)
}