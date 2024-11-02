package ru.notexe.alive.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import ru.notexe.alive.R
import ru.notexe.alive.presentation.contract.SmallDropDownState
import ru.notexe.alive.presentation.widgets.AliveDropDown
import ru.notexe.alive.ui.theme.AliveColors
import ru.notexe.alive.ui.theme.AliveTheme

@Composable
internal fun SmallDropDown(
    smallDropDown: SmallDropDownState?,
    isPalletActive: Boolean,
    onPalletClick: () -> Unit,
    onColorClick: (Color) -> Unit,
    onHeightChange: (Dp) -> Unit,
    onDismiss: () -> Unit,
) {
    val density = LocalDensity.current

    AliveDropDown(
        modifier = Modifier.onGloballyPositioned {
            onHeightChange(with(density) { it.size.height.toDp() })
        },
        offset = DpOffset(
            x = 0.dp,
            y = (-16).dp,
        ),
        expanded = smallDropDown != null,
        onDismiss = onDismiss,
    ) {
        when (smallDropDown) {
            SmallDropDownState.SMALL_PALLET -> {
                val smallPallet = remember {
                    createSmallPallet()
                }
                // LazyRow is needed to flexibility increase the number of colors
                LazyRow(
                    modifier = Modifier.padding(
                        vertical = 18.dp,
                        horizontal = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    item {
                        ClickableIcon(
                            iconResource = R.drawable.ic_pallet_32,
                            onClick = onPalletClick,
                            tint = if (isPalletActive) AliveColors.greenPallet.original else AliveTheme.tokens.primary
                        )
                    }
                    items(smallPallet) { color ->
                        Spacer(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(32.dp)
                                .padding(2.dp)
                                .background(
                                    color = color,
                                    shape = CircleShape,
                                )
                                .clickable {
                                    onColorClick(color)
                                }
                        )
                    }
                }
            }

            SmallDropDownState.FIGURE -> {}
            null -> Unit
        }
    }
}

private fun createSmallPallet() = persistentListOf(
    AliveColors.white,
    AliveColors.orangePallet.veryDark,
    AliveColors.black2,
    AliveColors.bluePallet.veryDark,
)