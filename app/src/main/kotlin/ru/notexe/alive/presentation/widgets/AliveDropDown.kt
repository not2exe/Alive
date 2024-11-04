package ru.notexe.alive.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import ru.notexe.alive.theme.AliveColors
import ru.notexe.alive.theme.AliveTheme

@Composable
internal fun AliveDropDown(
    offset: DpOffset,
    expanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val density = LocalDensity.current
    if (expanded) {
        Popup(
            popupPositionProvider = remember(density, offset) {
                DropDownMenuPositionProvider(
                    with(density) {
                        IntOffset(
                            x = offset.x.roundToPx(),
                            y = offset.y.roundToPx(),
                        )
                    }
                )
            },
            content = {
                Box(
                    modifier = modifier
                        .clip(AliveTheme.shapes.roundedCornerShape16)
                        .background(AliveColors.black.copy(alpha = 0.14f))
                        .border(
                            width = 1.dp,
                            color = AliveColors.grey,
                            shape = AliveTheme.shapes.roundedCornerShape16,
                        ),
                    content = content,
                )
            },
            onDismissRequest = onDismiss,
        )
    }
}