package ru.notexe.alive.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.notexe.alive.R
import ru.notexe.alive.presentation.contract.TopInteractionsActions

@Composable
internal fun TopInteractionsRow(
    topInteractionsActions: TopInteractionsActions,
) {
    Row(
        modifier = Modifier.height(32.dp)
    ) {
        ClickableIcon(
            iconResource = R.drawable.ic_arrow_left_24,
            onClick = topInteractionsActions::onUndoClick,
            innerPaddings = PaddingValues(4.dp),
        )
        ClickableIcon(
            iconResource = R.drawable.ic_arrow_right_24,
            onClick = topInteractionsActions::onRedoClick,
            innerPaddings = PaddingValues(4.dp),
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
            iconResource = R.drawable.ic_layers_32,
            onClick = topInteractionsActions::onAddFrameClick,
        )

        Spacer(modifier = Modifier.weight(1f))

        ClickableIcon(
            iconResource = R.drawable.ic_pause_32,
            onClick = topInteractionsActions::onRemoveClick,
        )
        ClickableIcon(
            iconResource = R.drawable.ic_play_32,
            onClick = topInteractionsActions::onAddFrameClick,
        )
    }
}