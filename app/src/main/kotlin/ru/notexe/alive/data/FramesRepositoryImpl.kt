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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.notexe.alive.data.models.FramesResponse
import ru.notexe.alive.domain.Frame
import ru.notexe.alive.domain.FramesRepository
import ru.notexe.alive.domain.FramesRepository.Companion.PAGE_SIZE
import ru.notexe.alive.domain.LoadPackOptions
import ru.notexe.alive.domain.PaintObject
import ru.notexe.alive.runSuspendCatching

internal class FramesRepositoryImpl(
    private val framesDatabase: FramesDatabase,
    private val framesDao: FramesDao,
    private val framesMapper: FramesMapper,
) : FramesRepository {
    init {
//        GlobalScope.launch(Dispatchers.IO) {
//            deleteFrameById(3)
//        }
    }

    init {
//        val lines =
//            "[{\"startX\":91.29436,\"startY\":1763.9641,\"endX\":91.49707,\"endY\":1763.6045},{\"startX\":91.49707,\"startY\":1763.6045,\"endX\":107.987305,\"endY\":1735.6201},{\"startX\":107.987305,\"startY\":1735.6201,\"endX\":118.77344,\"endY\":1719.3164},{\"startX\":118.77344,\"startY\":1719.3164,\"endX\":123.7666,\"endY\":1713.2803},{\"startX\":123.7666,\"startY\":1713.2803,\"endX\":128.22559,\"endY\":1707.084},{\"startX\":128.22559,\"startY\":1707.084,\"endX\":133.78125,\"endY\":1701.2188},{\"startX\":133.78125,\"startY\":1701.2188,\"endX\":138.57129,\"endY\":1695.5146},{\"startX\":138.57129,\"startY\":1695.5146,\"endX\":140.36426,\"endY\":1693.3623},{\"startX\":140.36426,\"startY\":1693.3623,\"endX\":147.06738,\"endY\":1684.2432},{\"startX\":147.06738,\"startY\":1684.2432,\"endX\":151.70605,\"endY\":1678.9531},{\"startX\":151.70605,\"startY\":1678.9531,\"endX\":155.40527,\"endY\":1671.541},{\"startX\":155.40527,\"startY\":1671.541,\"endX\":160.69727,\"endY\":1665.9629},{\"startX\":160.69727,\"startY\":1665.9629,\"endX\":163.05957,\"endY\":1658.5283},{\"startX\":163.05957,\"startY\":1658.5283,\"endX\":167.37695,\"endY\":1654.2793},{\"startX\":167.37695,\"startY\":1654.2793,\"endX\":169.99805,\"endY\":1649.3359},{\"startX\":169.99805,\"startY\":1649.3359,\"endX\":173.00879,\"endY\":1644.3184},{\"startX\":173.00879,\"startY\":1644.3184,\"endX\":174.66504,\"endY\":1640.669},{\"startX\":174.66504,\"startY\":1640.669,\"endX\":177.99219,\"endY\":1636.6777},{\"startX\":177.99219,\"startY\":1636.6777,\"endX\":177.0,\"endY\":1635.3848},{\"startX\":177.0,\"startY\":1635.3848,\"endX\":179.69727,\"endY\":1633.3027},{\"startX\":179.69727,\"startY\":1633.3027,\"endX\":180.33398,\"endY\":1631.333},{\"startX\":180.33398,\"startY\":1631.333,\"endX\":182.7002,\"endY\":1629.2998},{\"startX\":182.7002,\"startY\":1629.2998,\"endX\":183.34766,\"endY\":1627.3057},{\"startX\":183.34766,\"startY\":1627.3057,\"endX\":183.0,\"endY\":1626.7197},{\"startX\":183.0,\"startY\":1626.7197,\"endX\":183.0,\"endY\":1625.6309},{\"startX\":183.0,\"startY\":1625.6309,\"endX\":184.27441,\"endY\":1626.0},{\"startX\":184.27441,\"startY\":1626.0,\"endX\":184.0,\"endY\":1624.6416},{\"startX\":184.0,\"startY\":1624.6416,\"endX\":185.0,\"endY\":1625.0},{\"startX\":185.0,\"startY\":1625.0,\"endX\":185.0,\"endY\":1626.3262},{\"startX\":185.0,\"startY\":1626.3262,\"endX\":185.0,\"endY\":1628.6787},{\"startX\":185.0,\"startY\":1628.6787,\"endX\":185.0,\"endY\":1630.6602},{\"startX\":185.0,\"startY\":1630.6602,\"endX\":186.33594,\"endY\":1634.0078},{\"startX\":186.33594,\"startY\":1634.0078,\"endX\":186.0,\"endY\":1635.7021},{\"startX\":186.0,\"startY\":1635.7021,\"endX\":187.34277,\"endY\":1639.0273},{\"startX\":187.34277,\"startY\":1639.0273,\"endX\":185.65039,\"endY\":1640.6992},{\"startX\":185.65039,\"startY\":1640.6992,\"endX\":186.0,\"endY\":1644.0273},{\"startX\":186.0,\"startY\":1644.0273,\"endX\":187.35156,\"endY\":1645.7041},{\"startX\":187.35156,\"startY\":1645.7041,\"endX\":188.27246,\"endY\":1648.8184},{\"startX\":188.27246,\"startY\":1648.8184,\"endX\":189.36914,\"endY\":1653.4756},{\"startX\":189.36914,\"startY\":1653.4756,\"endX\":190.27539,\"endY\":1655.8262},{\"startX\":190.27539,\"startY\":1655.8262,\"endX\":192.73145,\"endY\":1659.0967},{\"startX\":192.73145,\"startY\":1659.0967,\"endX\":193.33594,\"endY\":1662.0078},{\"startX\":193.33594,\"startY\":1662.0078,\"endX\":195.70508,\"endY\":1667.7617},{\"startX\":195.70508,\"startY\":1667.7617,\"endX\":197.68945,\"endY\":1668.6895},{\"startX\":197.68945,\"startY\":1668.6895,\"endX\":199.70117,\"endY\":1673.4014},{\"startX\":199.70117,\"startY\":1673.4014,\"endX\":201.6875,\"endY\":1677.374},{\"startX\":201.6875,\"startY\":1677.374,\"endX\":204.99023,\"endY\":1679.9902},{\"startX\":204.99023,\"startY\":1679.9902,\"endX\":207.99902,\"endY\":1684.331},{\"startX\":207.99902,\"startY\":1684.331,\"endX\":209.6211,\"endY\":1688.2412},{\"startX\":209.6211,\"startY\":1688.2412,\"endX\":211.66895,\"endY\":1692.3369},{\"startX\":211.66895,\"startY\":1692.3369,\"endX\":212.33496,\"endY\":1695.0049},{\"startX\":212.33496,\"startY\":1695.0049,\"endX\":213.33496,\"endY\":1698.0049},{\"startX\":213.33496,\"startY\":1698.0049,\"endX\":213.0,\"endY\":1699.6787},{\"startX\":213.0,\"startY\":1699.6787,\"endX\":214.33398,\"endY\":1701.668},{\"startX\":214.33398,\"startY\":1701.668,\"endX\":215.34668,\"endY\":1703.6934},{\"startX\":215.34668,\"startY\":1703.6934,\"endX\":215.0,\"endY\":1705.6826},{\"startX\":215.0,\"startY\":1705.6826,\"endX\":215.0,\"endY\":1707.6758},{\"startX\":215.0,\"startY\":1707.6758,\"endX\":215.0,\"endY\":1708.3359},{\"startX\":215.0,\"startY\":1708.3359,\"endX\":217.69043,\"endY\":1712.0352}]"
//        val linesNotString = Json.decodeFromString<List<Line>>(lines)
//        GlobalScope.launch(Dispatchers.IO) {
//            var addX = 10f
//            var addY = 10f
//            repeat(Int.MAX_VALUE) {
//                val d = linesNotString.map {
//                    it.copy(
//                        startX = it.startX + addX,
//                        startY = it.startY + addY,
//                        endX = it.endX + addX,
//                        endY = it.endY + addY,
//                    )
//                }
//                addFrame(
//                    frame = Frame(
//                        id = 0,
//                        paintObjects = listOf(
//                            PaintObject(
//                                lines = d,
//                                lineStrokeWidth = 6f,
//                                colorProperties = ColorProperties(
//                                    0f, 0f, 0f, 1f
//                                ),
//                                paintingMode = PaintMode.PENCIL,
//                            ),
//                        )
//                    )
//                )
//                if (addX == 100f) {
//                    addX = 10f
//                    addY = 10f
//                }
//                addX += 1
//                addY += 1
//            }
//        }
    }

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
        val lastId = framesDao.lastId()
        val from = if (lastLoadedId + 1 <= lastId) lastLoadedId + 1 else 0
        val last = from + PAGE_SIZE - 1
        val resultFramesPack = mutableListOf<FramesResponse>()

        resultFramesPack.addAll(
            elements = framesDao.getFrames(
                from = from,
                last = lastLoadedId,
            )
        )
        if (resultFramesPack.size < PAGE_SIZE) {
            val newFrom = if (last < lastId) last + 1 else 1
            resultFramesPack.addAll(
                elements = framesDao.getFrames(
                    from = newFrom,
                    last = from + PAGE_SIZE - resultFramesPack.size - 1,
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
                last = from + PAGE_SIZE,
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