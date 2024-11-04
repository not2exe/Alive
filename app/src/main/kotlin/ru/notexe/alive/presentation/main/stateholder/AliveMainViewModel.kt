package ru.notexe.alive.presentation.main.stateholder

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.notexe.alive.domain.Frame
import ru.notexe.alive.domain.FramesRepository
import ru.notexe.alive.presentation.main.contract.AliveMainState
import ru.notexe.alive.presentation.main.contract.BigDropDownState
import ru.notexe.alive.presentation.main.contract.BottomInteractionsActions
import ru.notexe.alive.presentation.main.contract.ChangePresentation
import ru.notexe.alive.presentation.main.contract.FramePresentation
import ru.notexe.alive.presentation.main.contract.PaintObjectPresentation
import ru.notexe.alive.presentation.main.contract.PaintingMode
import ru.notexe.alive.presentation.main.contract.SmallDropDownState
import ru.notexe.alive.presentation.main.contract.TopInteractionsActions

internal class AliveMainViewModel(
    private val framesRepository: FramesRepository,
    private val framesPresentationMapper: FramesPresentationMapper,
) : ViewModel(),
    TopInteractionsActions,
    BottomInteractionsActions {

    private val _screenState = MutableStateFlow(
        AliveMainState()
    )

    init {
        loadInitialPack()
        collectFramesCount()
    }

    val screenState = _screenState.asStateFlow()

    fun onNewPaintObjectAdded(
        start: Offset,
        changes: ImmutableList<ChangePresentation>,
        path: Path,
    ) {
        _screenState.update { state ->
            state.copy(
                currentPaintingFrame = state
                    .currentPaintingFrame
                    .updatePaintObjects {
                        add(
                            PaintObjectPresentation(
                                path = path,
                                color = state.paintingSettings.currentColor,
                                strokeWidth = state.paintingSettings.strokeWidth,
                                paintingMode = state.paintingSettings.paintingMode,
                                changes = changes,
                                start = start,
                            )
                        )
                    }
            )
        }
    }

    override fun onUndoClick() {
        _screenState.update { state ->
            val last = state.currentPaintingFrame.paintObjects.lastOrNull() ?: return
            state.copy(
                currentPaintingFrame = state
                    .currentPaintingFrame
                    .updatePaintObjects {
                        removeAt(lastIndex)
                    },
                redoList = state
                    .redoList
                    .toMutableList()
                    .apply { add(last) }
                    .toImmutableList(),
            )
        }
    }

    override fun onRedoClick() {
        _screenState.update { state ->
            val last = state.redoList.lastOrNull() ?: return
            state.copy(
                currentPaintingFrame = state
                    .currentPaintingFrame
                    .updatePaintObjects {
                        add(last)
                    },
                redoList = state.redoList.dropLast(1).toImmutableList(),
            )
        }
    }

    override fun onRemoveClick() {
        viewModelScope.launch {
            withLoading {
                val currentFrame = screenState.value.currentPaintingFrame
                if (currentFrame.isNew) {
                    updateCurrentAndPrevious(framesRepository.currentPackFrames.value)
                    return@withLoading
                }
                framesRepository
                    .deleteFrameById(currentFrame.id)
                    .onSuccess { framePack ->
                        updateCurrentAndPrevious(framePack)
                    }
            }
        }
    }

    override fun onAddFrameClick() {
        addFrameInternal(doubleCurrent = false)
    }

    override fun onDoubleClick() {
        addFrameInternal(doubleCurrent = true)
    }

    override fun onShowFramesClick() {
        TODO("Not yet implemented")
    }

    override fun onPauseClick() {
        _screenState.update {
            it.copy(
                currentAnimationFrame = null,
                isAnimationPlaying = false,
            )
        }
    }

    override fun onPlayClick() {
        viewModelScope.launch {
            addOrUpdateFrame(screenState.value.currentPaintingFrame)
            framesRepository.clearAnimationCache()
            loadNextPack(0)
            _screenState.update { state ->
                state.copy(
                    isAnimationPlaying = true,
                )
            }
            val framesCount = screenState.value.framesCount

            framesRepository
                .animationPackFramesFlow
                .map(framesPresentationMapper::mapFrames)
                .collect { pack ->
                    if (framesCount - FramesRepository.PAGE_SIZE <= 0) {
                        cycleFramePack(
                            parentScope = this,
                            frames = pack,
                        )
                    } else {
                        handleNextPack(
                            parentScope = this,
                            frames = pack,
                        )
                    }
                }
        }
    }

    override fun onPaintingModeChanged(paintingMode: PaintingMode) {
        _screenState.update { state ->
            state.copy(
                paintingSettings = state.paintingSettings.copy(
                    paintingMode = paintingMode,
                    strokeWidth = ((paintingMode.strokeWidthRange.last - paintingMode.strokeWidthRange.first) / 2).dp
                )
            )
        }
    }

    override fun onColorClick(color: Color) {
        _screenState.update { state ->
            state.copy(
                paintingSettings = state.paintingSettings.copy(
                    currentColor = color,
                ),
                bigDropDown = null,
                smallDropDownState = null,
            )
        }
    }

    override fun onShapeAddClick() {
        _screenState.update {
            it.copy(
                smallDropDownState = SmallDropDownState.FIGURE,
            )
        }
    }

    override fun onPalletClick() {
        _screenState.update { state ->
            state.copy(
                bigDropDown = BigDropDownState.BIG_PALLET,
            )
        }
    }

    override fun onDropDownDismiss() {
        _screenState.update { state ->
            state.copy(
                bigDropDown = null,
                smallDropDownState = null,
            )
        }
    }

    override fun onColorChangeClick() {
        _screenState.update {
            it.copy(
                smallDropDownState = SmallDropDownState.SMALL_PALLET,
            )
        }
    }

    private fun loadNextPack(lastFrameId: Long) = viewModelScope.launch {
        framesRepository.loadNextCycled(lastFrameId)
    }

    private suspend fun handleNextPack(
        parentScope: CoroutineScope,
        frames: List<FramePresentation>,
    ) {
        val lastFrameId = frames.lastOrNull()?.id ?: 0
        frames.forEachIndexed { index, frame ->
            if (!screenState.value.isAnimationPlaying) {
                parentScope.cancel()
                return
            }

            if (frames.size - index.toLong() == FramesRepository.PAGE_SIZE / 2) {
                loadNextPack(lastFrameId)
            }

            _screenState.update { state ->
                if (state.isAnimationPlaying) {
                    state.copy(
                        currentAnimationFrame = frame.paintObjects,
                    )
                } else {
                    state
                }
            }
            delay(100)
        }
    }

    private suspend fun cycleFramePack(
        parentScope: CoroutineScope,
        frames: List<FramePresentation>
    ) {
        var currentFrameIndex = 0
        while (screenState.value.isAnimationPlaying) {
            _screenState.update { state ->
                if (state.isAnimationPlaying) {
                    state.copy(
                        currentAnimationFrame = frames[currentFrameIndex].paintObjects,
                    )
                } else {
                    state
                }
            }
            delay(100)
            currentFrameIndex = (currentFrameIndex + 1) % frames.size
        }
        parentScope.cancel()
    }

    private fun collectFramesCount() {
        viewModelScope.launch {
            framesRepository.framesCount().collectLatest { count ->
                _screenState.update {
                    it.copy(
                        framesCount = count,
                    )
                }
            }
        }
    }

    private fun loadInitialPack() = viewModelScope.launch {
        withLoading {
            val pack = framesRepository.loadInitialPack().orEmpty()
            updateCurrentAndPrevious(pack)
        }
    }

    private fun addFrameInternal(doubleCurrent: Boolean) {
        viewModelScope.launch {
            withLoading {
                val currentFrame = screenState.value.currentPaintingFrame
                addOrUpdateFrame(currentFrame = currentFrame)
                    .onSuccess {
                        val last = framesRepository.currentPackFrames.value.lastOrNull()?.let(framesPresentationMapper::mapFrame)
                        _screenState.update { state ->
                            state.copy(
                                currentPaintingFrame = if (doubleCurrent) currentFrame else FramePresentation(),
                                previousPaintingFrame = last ?: FramePresentation(),
                            )
                        }
                    }
            }
        }
    }

    private suspend fun addOrUpdateFrame(currentFrame: FramePresentation): Result<List<Frame>> {
        if (currentFrame.paintObjects.isEmpty()) return Result.failure(Exception())
        val domainFrame = framesPresentationMapper.mapPaintObjectsToDomainFrame(currentFrame)
        return if (currentFrame.isNew) {
            framesRepository.addFrame(
                frame = domainFrame,
            )
        } else {
            framesRepository.updateFrame(frame = domainFrame)
        }
    }

    private fun updateCurrentAndPrevious(framesPack: List<Frame>) {
        _screenState.update { state ->
            state.copy(
                currentPaintingFrame = framesPack.getOrNull(framesPack.lastIndex)?.let(framesPresentationMapper::mapFrame) ?: FramePresentation(),
                previousPaintingFrame = framesPack.getOrNull(framesPack.lastIndex - 1)?.let(framesPresentationMapper::mapFrame) ?: FramePresentation(),
                loading = false,
            )
        }
    }

    private suspend fun withLoading(
        block: suspend () -> Unit,
    ) {
        _screenState.update {
            it.copy(
                loading = true,
            )
        }
        block()
        _screenState.update {
            it.copy(
                loading = false,
            )
        }
    }
}