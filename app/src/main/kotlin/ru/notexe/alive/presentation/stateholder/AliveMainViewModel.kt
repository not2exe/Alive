package ru.notexe.alive.presentation.stateholder

import androidx.compose.ui.graphics.Color
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
import ru.notexe.alive.presentation.FramePresentation
import ru.notexe.alive.presentation.FramesPresentationMapper
import ru.notexe.alive.presentation.contract.AliveMainState
import ru.notexe.alive.presentation.contract.BigDropDownState
import ru.notexe.alive.presentation.contract.BottomInteractionsActions
import ru.notexe.alive.presentation.contract.LinePresentation
import ru.notexe.alive.presentation.contract.PaintObjectPresentation
import ru.notexe.alive.presentation.contract.PaintingMode
import ru.notexe.alive.presentation.contract.SmallDropDownState
import ru.notexe.alive.presentation.contract.TopInteractionsActions

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

    fun onNewPaintObjectAdded(lines: ImmutableList<LinePresentation>) {
        _screenState.update { state ->
            state.copy(
                currentPaintingFrame = state
                    .currentPaintingFrame
                    .updatePaintObjects {
                        add(
                            PaintObjectPresentation(
                                lines = lines,
                                color = state.paintingSettings.currentColor,
                                strokeWidth = state.paintingSettings.strokeWidth,
                                paintingMode = state.paintingSettings.paintingMode,
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
            val currentFrame = screenState.value.currentPaintingFrame
            if (currentFrame.isNew) {
                handleRemoveNew()
                return@launch
            }

            framesRepository.deleteFrameById(currentFrame.id).onSuccess { framePack ->
                _screenState.update { state ->
                    state.copy(
                        currentPaintingFrame = framePack
                            .lastOrNull()
                            ?.let(framesPresentationMapper::mapFrame) ?: FramePresentation(),
                    )
                }
            }
        }
    }

    override fun onAddFrameClick() {
        val currentFrame = screenState.value.currentPaintingFrame
        if (currentFrame.paintObjects.isEmpty()) {
            return
        }
        viewModelScope.launch {
            addOrUpdateFrame(currentFrame = currentFrame)
                .onSuccess {
                    _screenState.update { state ->
                        state.copy(
                            currentPaintingFrame = FramePresentation(),
                        )
                    }
                }
        }
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
        if (frames.size < FramesRepository.PAGE_SIZE / 2) {
            loadNextPack(lastFrameId)
        }
        frames.forEachIndexed { index, frame ->
            if (!screenState.value.isAnimationPlaying) {
                parentScope.cancel()
                return
            }

            if (frames.size - index == FramesRepository.PAGE_SIZE / 2) {
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
        val last = framesRepository.loadInitialPack()?.lastOrNull() ?: return@launch
        _screenState.update { state ->
            state.copy(
                currentPaintingFrame = framesPresentationMapper.mapFrame(last)
            )
        }
    }

    private suspend fun addOrUpdateFrame(currentFrame: FramePresentation): Result<List<Frame>> {
        val domainFrame = framesPresentationMapper.mapPaintObjectsToDomainFrame(currentFrame)
        return if (currentFrame.isNew) {
            framesRepository.addFrame(
                frame = domainFrame,
            )
        } else {
            framesRepository.updateFrame(frame = domainFrame)
        }
    }

    private fun handleRemoveNew() {
        val last = framesRepository.currentPackFrames.value.lastOrNull()?.let(framesPresentationMapper::mapFrame)
        _screenState.update { state ->
            state.copy(
                currentPaintingFrame = last ?: FramePresentation(),
            )
        }
    }
}