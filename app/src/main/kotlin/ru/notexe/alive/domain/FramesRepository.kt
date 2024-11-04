package ru.notexe.alive.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface FramesRepository {

    val currentPackFrames: StateFlow<List<Frame>>

    val animationPackFramesFlow: Flow<List<Frame>>

    fun framesCount(): Flow<Long>

    fun clearAnimationCache()

    suspend fun loadInitialPack(): List<Frame>?

    suspend fun loadPrevious(
        firstLoadedId: Long,
        loadPackOptions: LoadPackOptions = LoadPackOptions.MERGE,
    ): List<Frame>?

    suspend fun loadNextCycled(
        lastLoadedId: Long,
    )

    suspend fun addFrame(frame: Frame): Result<List<Frame>>

    suspend fun updateFrame(frame: Frame): Result<List<Frame>>

    suspend fun deleteFrameById(id: Long): Result<List<Frame>>

    fun clearCurrentPack()

    companion object {
        const val PAGE_SIZE = 150L
    }
}