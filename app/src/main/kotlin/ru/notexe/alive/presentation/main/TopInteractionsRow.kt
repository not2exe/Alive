package ru.notexe.alive.presentation.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.notexe.alive.R
import ru.notexe.alive.presentation.ClickableIcon
import ru.notexe.alive.presentation.main.contract.TopInteractionsActions

@Composable
internal fun ColumnScope.TopInteractionsRow(
    playEnabled: Boolean,
    pauseEnabled: Boolean,
    undoEnabled: Boolean,
    redoEnabled: Boolean,
    isAnimationPlaying: Boolean,
    topInteractionsActions: TopInteractionsActions,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .animateContentSize()
            .align(Alignment.CenterHorizontally)
    ) {
        if (!isAnimationPlaying) {
            ClickableIcon(
                iconResource = R.drawable.ic_arrow_left_24,
                onClick = topInteractionsActions::onUndoClick,
                innerPaddings = PaddingValues(4.dp),
                enabled = undoEnabled,
            )
            ClickableIcon(
                iconResource = R.drawable.ic_arrow_right_24,
                onClick = topInteractionsActions::onRedoClick,
                innerPaddings = PaddingValues(4.dp),
                enabled = redoEnabled,
            )

            Spacer(modifier = Modifier.weight(1f))

            ClickableIcon(
                iconResource = R.drawable.ic_bin_32,
                onClick = topInteractionsActions::onRemoveClick,
            )

            ClickableIcon(
                iconResource = R.drawable.ic_frame_plus_32,
                onClick = topInteractionsActions::onAddFrameClick,
            )
            ClickableIcon(
                iconResource = R.drawable.ic_settings_32,
                onClick = onSettingsClick,
            )
            ClickableIcon(
                iconResource = R.drawable.ic_stack_28,
                onClick = topInteractionsActions::onDoubleClick,
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        ClickableIcon(
            iconResource = R.drawable.ic_pause_32,
            onClick = topInteractionsActions::onPauseClick,
            enabled = pauseEnabled,
        )
        if (!isAnimationPlaying) {
            ClickableIcon(
                iconResource = R.drawable.ic_play_32,
                onClick = topInteractionsActions::onPlayClick,
                enabled = playEnabled,
            )
        }
    }
}