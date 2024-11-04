package ru.notexe.alive.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.notexe.alive.presentation.paint.PaintZone
import ru.notexe.alive.presentation.stateholder.AliveMainViewModel
import ru.notexe.alive.ui.theme.AliveTheme

@Composable
internal fun AliveMainScreen(
    aliveMainViewModel: AliveMainViewModel,
) {
    val state by aliveMainViewModel.screenState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AliveTheme.tokens.background)
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TopInteractionsRow(
            playEnabled = state.playEnabled,
            pauseEnabled = state.pauseEnabled,
            undoEnabled = state.undoEnabled,
            redoEnabled = state.redoEnabled,
            topInteractionsActions = aliveMainViewModel
        )
        Spacer(modifier = Modifier.height(32.dp))
        PaintZone(
            paintingSettings = state.paintingSettings,
            currentAnimationFrame = state.currentAnimationFrame,
            onNewPaintObjectAdded = aliveMainViewModel::onNewPaintObjectAdded,
            framePaintObjects = state.currentPaintingFrame.paintObjects,
        )
        Spacer(modifier = Modifier.height(22.dp))
        BottomInteractionsRow(
            currentColor = state.paintingSettings.currentColor,
            currentPaintingMode = state.paintingSettings.paintingMode,
            bottomInteractionsActions = aliveMainViewModel,
            smallDropDown = state.smallDropDownState,
            bigDropDown = state.bigDropDown,
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}