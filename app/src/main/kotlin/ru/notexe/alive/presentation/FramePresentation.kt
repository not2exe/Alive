package ru.notexe.alive.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.notexe.alive.presentation.contract.PaintObjectPresentation

internal data class FramePresentation(
    val id: Long = NEW_FRAME_ID,
    val paintObjects: ImmutableList<PaintObjectPresentation> = persistentListOf(),
) {

    val isNew = id == NEW_FRAME_ID

    fun updatePaintObjects(
        update: MutableList<PaintObjectPresentation>.() -> Unit,
    ): FramePresentation = copy(
        paintObjects = paintObjects
            .toMutableList()
            .apply {
                update()
            }
            .toImmutableList()
    )

    private companion object {
        const val NEW_FRAME_ID = -1L
    }
}