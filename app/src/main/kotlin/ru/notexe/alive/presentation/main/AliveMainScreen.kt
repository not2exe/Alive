package ru.notexe.alive.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.notexe.alive.presentation.main.paint.PaintZone
import ru.notexe.alive.presentation.main.stateholder.AliveMainViewModel
import ru.notexe.alive.presentation.settings.navigation.SettingsRoute
import ru.notexe.alive.theme.AliveTheme

@Composable
internal fun AliveMainScreen(
    aliveMainViewModel: AliveMainViewModel,
    onNavigate: (route: Any) -> Unit,
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
            isAnimationPlaying = state.isAnimationPlaying,
            topInteractionsActions = aliveMainViewModel,
            onSettingsClick = {
                onNavigate(SettingsRoute)
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        PaintZone(
            paintingSettings = state.paintingSettings,
            currentAnimationFrame = state.currentAnimationFrame,
            onNewPaintObjectAdded = aliveMainViewModel::onNewPaintObjectAdded,
            currentFrame = state.currentPaintingFrame.paintObjects,
            previousFrame = state.previousPaintingFrame.paintObjects,
            isAnimationPlaying = state.isAnimationPlaying,
        )
        Spacer(modifier = Modifier.height(22.dp))
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            visible = !state.isAnimationPlaying,
        ) {
            BottomInteractionsRow(
                currentColor = state.paintingSettings.currentColor,
                currentPaintingMode = state.paintingSettings.paintingMode,
                bottomInteractionsActions = aliveMainViewModel,
                smallDropDown = state.smallDropDownState,
                bigDropDown = state.bigDropDown,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
    AnimatedVisibility(
        state.loading,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    AliveTheme.tokens.background.copy(alpha = 0.2f)
                )
                .pointerInput(Unit) {}
        )
    }
}