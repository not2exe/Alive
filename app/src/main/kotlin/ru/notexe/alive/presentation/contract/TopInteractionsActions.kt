package ru.notexe.alive.presentation.contract

import androidx.compose.runtime.Immutable

@Immutable
interface TopInteractionsActions {
    fun onUndoClick()
    fun onRedoClick()
    fun onRemoveClick()
    fun onAddFrameClick()
    fun onShowFramesClick()
    fun onPauseClick()
    fun onPlayClick()
}