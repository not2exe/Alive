package ru.notexe.alive.presentation.main.contract

import androidx.compose.runtime.Immutable

@Immutable
interface TopInteractionsActions {
    fun onUndoClick()
    fun onRedoClick()
    fun onRemoveClick()
    fun onAddFrameClick()
    fun onPauseClick()
    fun onPlayClick()
    fun onDoubleClick()
}