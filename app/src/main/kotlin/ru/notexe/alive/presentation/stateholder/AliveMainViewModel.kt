package ru.notexe.alive.presentation.stateholder

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.notexe.alive.presentation.contract.AliveMainState
import ru.notexe.alive.presentation.contract.BigDropDownState
import ru.notexe.alive.presentation.contract.BottomInteractionsActions
import ru.notexe.alive.presentation.contract.Line
import ru.notexe.alive.presentation.contract.PaintObject
import ru.notexe.alive.presentation.contract.PaintingMode
import ru.notexe.alive.presentation.contract.SmallDropDownState
import ru.notexe.alive.presentation.contract.TopInteractionsActions

internal class AliveMainViewModel :
    ViewModel(),
    TopInteractionsActions,
    BottomInteractionsActions {

    private val _screenState = MutableStateFlow(
        AliveMainState()
    )

    // TODO move to db
    private val framesAnimation = mutableStateListOf<ImmutableList<PaintObject>>()

    val screenState = _screenState.asStateFlow()

    fun onNewPaintObjectAdded(lines: ImmutableList<Line>) {
        _screenState.update { state ->
            state.copy(
                currentPaintingFrame = state
                    .currentPaintingFrame
                    .toMutableList()
                    .apply {
                        add(
                            PaintObject(
                                lines = lines,
                                color = state.paintingSettings.currentColor,
                                strokeWidth = state.paintingSettings.strokeWidth,
                                paintingMode = state.paintingSettings.paintingMode,
                            )
                        )
                    }
                    .toImmutableList()
            )
        }
    }

    override fun onUndoClick() {
        _screenState.update { state ->
            val last = state.currentPaintingFrame.lastOrNull() ?: return
            state.copy(
                currentPaintingFrame = state
                    .currentPaintingFrame
                    .dropLast(1)
                    .toImmutableList(),
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
                    .toMutableList()
                    .apply { add(last) }
                    .toImmutableList(),
                redoList = state.redoList.dropLast(1).toImmutableList(),
            )
        }
    }

    override fun onRemoveClick() {
        _screenState.update { state ->
            val last = framesAnimation.removeLastIfExist()
            state.copy(
                currentPaintingFrame = last ?: persistentListOf(),
            )
        }
    }

    override fun onAddFrameClick() {
        _screenState.update { state ->
            if (state.currentPaintingFrame.isEmpty()) {
                return@update state
            }
            framesAnimation.add(state.currentPaintingFrame)
            state.copy(
                currentPaintingFrame = persistentListOf(),
                playEnabled = framesAnimation.size > 1,
            )
        }
    }

    override fun onShowFramesClick() {
        TODO("Not yet implemented")
    }

    override fun onPauseClick() {
        _screenState.update {
            val last = framesAnimation.removeLastIfExist()
            it.copy(
                currentAnimationFrame = null,
                currentPaintingFrame = last ?: persistentListOf(),
            )
        }
    }

    override fun onPlayClick() {
        onAddFrameClick()
        viewModelScope.launch {
            var currentFrameIndex = 0
            do {
                _screenState.update {
                    it.copy(
                        currentAnimationFrame = framesAnimation[currentFrameIndex]
                    )
                }
                currentFrameIndex = (currentFrameIndex + 1) % framesAnimation.size
                delay(100)
            } while (screenState.value.currentAnimationFrame != null)
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

    private fun MutableList<ImmutableList<PaintObject>>.removeLastIfExist() = if (lastIndex == -1) null else removeAt(lastIndex)
}