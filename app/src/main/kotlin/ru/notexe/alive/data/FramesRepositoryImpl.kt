package ru.notexe.alive.data

import androidx.compose.ui.util.fastAny
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.notexe.alive.data.models.FramesResponse
import ru.notexe.alive.domain.Frame
import ru.notexe.alive.domain.FramesRepository
import ru.notexe.alive.domain.FramesRepository.Companion.PAGE_SIZE
import ru.notexe.alive.domain.LoadPackOptions
import ru.notexe.alive.domain.PaintObject
import ru.notexe.alive.utils.runSuspendCatching

internal class FramesRepositoryImpl(
    private val framesDatabase: FramesDatabase,
    private val framesDao: FramesDao,
    private val framesMapper: FramesMapper,
) : FramesRepository {

    private val _currentPackFrames = MutableStateFlow<List<Frame>>(
        listOf()
    )

    override val currentPackFrames: StateFlow<List<Frame>>
        get() = _currentPackFrames.asStateFlow()

    private val _animationPackFramesFlow = MutableSharedFlow<List<Frame>>(
        replay = 1,
    )

    override val animationPackFramesFlow: Flow<List<Frame>>
        get() = _animationPackFramesFlow.asSharedFlow()

    override fun framesCount(): Flow<Long> = framesDao.count()

    override suspend fun loadInitialPack(): List<Frame> = withContext(Dispatchers.IO) {
        val lastId = framesDao.lastId()
        loadPrevious(
            firstLoadedId = lastId,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun clearAnimationCache() {
        _animationPackFramesFlow.resetReplayCache()
    }

    override suspend fun loadNextCycled(
        lastLoadedId: Long,
    ) = withContext(Dispatchers.IO) {
        val count = framesCount().first()
        val lastId = framesDao.lastId()
        val from = if (lastLoadedId + 1 <= lastId) lastLoadedId + 1 else 1
        val resultFramesPack = mutableListOf<FramesResponse>()

        resultFramesPack.addAll(
            elements = framesDao.getFrames(
                from = from,
                limit = PAGE_SIZE,
            )
        )
        if (resultFramesPack.size < PAGE_SIZE && count > PAGE_SIZE) {
            resultFramesPack.addAll(
                elements = framesDao.getFrames(
                    from = 1,
                    limit = PAGE_SIZE - resultFramesPack.size,
                )
            )
        }
        _animationPackFramesFlow.emit(
            framesMapper.map(resultFramesPack),
        )
    }

    override suspend fun addFrame(frame: Frame): Result<List<Frame>> = withContext(Dispatchers.IO) {
        val result = runSuspendCatching {
            framesDatabase.withTransaction {
                val frameId = framesDao.addFrame()
                if (frameId == NOT_INSERTED_VALUE) {
                    error("Frame was not inserted")
                }
                insertPaintObjects(
                    frameId = frameId,
                    paintObjects = frame.paintObjects
                )
            }
        }.onSuccess {
            updatePackIfLastOtherwiseLoadLast { currentPack ->
                currentPack + listOf(frame)
            }
            return@withContext Result.success(currentPackFrames.value)
        }

        Result.failure(result.exceptionOrNull() ?: IllegalStateException())
    }

    override suspend fun updateFrame(frame: Frame): Result<List<Frame>> = withContext(Dispatchers.IO) {
        val result = runSuspendCatching {
            framesDatabase.withTransaction {
                framesDao.deletePaintObjectsById(frame.id)
                insertPaintObjects(frame.id, frame.paintObjects)
            }
        }.onSuccess {
            updatePackIfLastOtherwiseLoadLast { currentPack ->
                val indexToReplace = currentPack
                    .indexOfLast { oldFrame -> oldFrame.id == frame.id }
                    .takeIf { it != -1 } ?: return@updatePackIfLastOtherwiseLoadLast currentPack

                val mutableList = currentPack.toMutableList()
                mutableList[indexToReplace] = frame
                mutableList
            }
            return@withContext Result.success(currentPackFrames.value)
        }
        Result.failure(result.exceptionOrNull() ?: IllegalStateException())
    }

    override suspend fun deleteFrameById(id: Long) = withContext(Dispatchers.IO) {
        if (framesDao.deleteFrameById(id) < 1) {
            Result.failure<List<Frame>>(Exception("Frame was not deleted"))
        }
        _currentPackFrames.update { currentPack ->
            currentPack.toMutableList().apply {
                val indexToRemove = currentPack
                    .indexOfLast { it.id == id }
                    .takeIf { it != -1 }
                indexToRemove?.let(::removeAt)
            }
        }

        Result.success(currentPackFrames.value)
    }

    override suspend fun loadPrevious(
        firstLoadedId: Long,
        loadPackOptions: LoadPackOptions,
    ): List<Frame> {
        if (firstLoadedId == 0L) {
            return emptyList()
        }
        val from = (firstLoadedId - PAGE_SIZE).coerceAtLeast(1)
        val newFrames = framesMapper.map(
            framesDao.getFrames(
                from = from,
                limit = PAGE_SIZE,
            )
        )

        _currentPackFrames.update { current ->
            when (loadPackOptions) {
                LoadPackOptions.MERGE -> newFrames + current
                LoadPackOptions.EMIT -> newFrames
            }
        }

        return currentPackFrames.value
    }

    private suspend fun insertPaintObjects(
        frameId: Long,
        paintObjects: List<PaintObject>,
    ) {
        if (framesDao.insertPaintObjects(
                paintObject = framesMapper.mapPaintObjectsToDto(
                    list = paintObjects,
                    frameId = frameId,
                )
            ).fastAny { rowId -> rowId == NOT_INSERTED_VALUE }
        ) {
            error("Paint objects were not inserted")
        }
    }

    private suspend fun updatePackIfLastOtherwiseLoadLast(
        update: (List<Frame>) -> List<Frame>,
    ) {
        val framesCount = framesDao.lastId()
        val lastId = currentPackFrames.value.lastOrNull()?.id ?: 0
        if (lastId == framesCount) {
            _currentPackFrames.update(update)
        } else {
            loadPrevious(
                firstLoadedId = framesCount,
                loadPackOptions = LoadPackOptions.EMIT,
            )
        }
    }

    companion object {
        const val NOT_INSERTED_VALUE = -1L
    }
}