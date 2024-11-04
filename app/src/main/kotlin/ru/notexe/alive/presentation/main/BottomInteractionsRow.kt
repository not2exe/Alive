package ru.notexe.alive.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.notexe.alive.presentation.ClickableIcon
import ru.notexe.alive.presentation.main.contract.BigDropDownState
import ru.notexe.alive.presentation.main.contract.BottomInteractionsActions
import ru.notexe.alive.presentation.main.contract.PaintingMode
import ru.notexe.alive.presentation.main.contract.SmallDropDownState
import ru.notexe.alive.theme.AliveColors
import ru.notexe.alive.theme.AliveTheme

@Composable
internal fun ColumnScope.BottomInteractionsRow(
    currentColor: Color,
    currentPaintingMode: PaintingMode,
    bottomInteractionsActions: BottomInteractionsActions,
    smallDropDown: SmallDropDownState?,
    bigDropDown: BigDropDownState?,
) {
    Row {
        PaintingMode.entries.forEach { mode ->
            ClickableIcon(
                iconResource = mode.iconResource,
                onClick = { bottomInteractionsActions.onPaintingModeChanged(mode) },
                tint = iconColor(mode == currentPaintingMode && smallDropDown == null),
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        val isPalletDropDownOpen = smallDropDown == SmallDropDownState.SMALL_PALLET
        Spacer(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable(onClick = bottomInteractionsActions::onColorChangeClick)
                .colorContrastBorder(
                    enable = currentColor == AliveTheme.tokens.background || isPalletDropDownOpen,
                    borderColor = if (isPalletDropDownOpen) AliveColors.greenPallet.original else AliveTheme.tokens.primary
                )
                .background(
                    color = currentColor,
                    shape = CircleShape,
                )
        )

        var smallDropDownHeight by remember { mutableStateOf(0.dp) }
        SmallDropDown(
            smallDropDown = smallDropDown,
            isPalletActive = bigDropDown != null,
            onPalletClick = bottomInteractionsActions::onPalletClick,
            onColorClick = bottomInteractionsActions::onColorClick,
            onDismiss = bottomInteractionsActions::onDropDownDismiss,
            onHeightChange = { height ->
                smallDropDownHeight = height
            }
        )
        BigDropDown(
            smallDropDownHeight = smallDropDownHeight,
            bigDropDownState = bigDropDown,
            onDismiss = bottomInteractionsActions::onDropDownDismiss,
            onColorClick = bottomInteractionsActions::onColorClick,
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
                1.dp
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape,
            )
    } else {
        Modifier.padding(2.dp)
    }
)

@Composable
private fun iconColor(active: Boolean) = if (active) AliveColors.greenPallet.original else AliveTheme.tokens.primary