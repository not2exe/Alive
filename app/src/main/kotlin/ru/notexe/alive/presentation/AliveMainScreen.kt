package ru.notexe.alive.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.notexe.alive.presentation.contract.BottomInteractionsActions
import ru.notexe.alive.presentation.contract.PaintingMode
import ru.notexe.alive.presentation.contract.TopInteractionsActions
import ru.notexe.alive.presentation.paint.PaintZone
import ru.notexe.alive.ui.theme.AliveTheme

@Composable
internal fun AliveMainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AliveTheme.tokens.background)
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TopInteractionsRow(
            topInteractionsActions = object :TopInteractionsActions{
                override fun onUndoClick() {
                    TODO("Not yet implemented")
                }

                override fun onRedoClick() {
                    TODO("Not yet implemented")
                }

                override fun onRemoveClick() {
                    TODO("Not yet implemented")
                }

                override fun onAddFrameClick() {
                    TODO("Not yet implemented")
                }

                override fun onShowFramesClick() {
                    TODO("Not yet implemented")
                }

                override fun onPauseClick() {
                    TODO("Not yet implemented")
                }

                override fun onPlayClick() {
                    TODO("Not yet implemented")
                }

            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        PaintZone(
            currentAnimationFrame = null,
            color = Color.Black,
            strokeWidth = 4.dp,
        )
        Spacer(modifier = Modifier.height(22.dp))
        BottomInteractionsRow(
            currentColor = Color.Black,
            currentPaintingMode = PaintingMode.PENCIL,
            bottomInteractionsActions = object : BottomInteractionsActions {
                override fun onPaintingModeChanged(paintingMode: PaintingMode) {
                    TODO("Not yet implemented")
                }

                override fun onShapeAddClick() {
                    TODO("Not yet implemented")
                }

                override fun onColorChanged() {
                    TODO("Not yet implemented")
                }

            }
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}