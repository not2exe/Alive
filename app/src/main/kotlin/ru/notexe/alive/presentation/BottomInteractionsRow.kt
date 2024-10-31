package ru.notexe.alive.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.notexe.alive.presentation.contract.BottomInteractionsActions
import ru.notexe.alive.presentation.contract.PaintingMode
import ru.notexe.alive.ui.theme.AliveColors
import ru.notexe.alive.ui.theme.AliveTheme

@Composable
internal fun ColumnScope.BottomInteractionsRow(
    currentColor: Color,
    currentPaintingMode: PaintingMode,
    bottomInteractionsActions: BottomInteractionsActions,
) {
    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PaintingMode.entries.forEach { mode ->
            ClickableIcon(
                iconResource = mode.iconResource,
                onClick = { bottomInteractionsActions.onPaintingModeChanged(mode) },
                tint = iconColor(mode == currentPaintingMode),
            )
        }
        Spacer(
            modifier = Modifier
                .colorContrastBorder(
                    currentColor == AliveTheme.tokens.background,
                    borderColor = AliveTheme.tokens.primary
                )
                .size(28.dp)
                .clip(CircleShape)
                .background(currentColor)
        )
    }
}

/**
 * This frame is needed when the colors of our background and the selected color for drawing match
 */
private fun Modifier.colorContrastBorder(
    enable: Boolean,
    borderColor: Color,
) = then(
    if (enable) {
        Modifier
            .padding(
                vertical = 1.dp
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape,
            )
    } else {
        Modifier.padding(vertical = 2.dp)
    }
)

@Composable
private fun iconColor(active: Boolean) = if (active) AliveColors.toxicGreen else AliveTheme.tokens.primary