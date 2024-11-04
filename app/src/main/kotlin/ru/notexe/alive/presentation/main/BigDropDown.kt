package ru.notexe.alive.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.notexe.alive.presentation.main.contract.BigDropDownState
import ru.notexe.alive.presentation.widgets.AliveDropDown
import ru.notexe.alive.theme.AliveColors

@Composable
internal fun BigDropDown(
    smallDropDownHeight: Dp = 0.dp,
    bigDropDownState: BigDropDownState? = null,
    onColorClick: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    AliveDropDown(
        modifier = Modifier,
        offset = DpOffset(
            x = 0.dp,
            y = -(32.dp + smallDropDownHeight),
        ),
        expanded = bigDropDownState != null,
        onDismiss = onDismiss,
    ) {
        when (bigDropDownState) {
            BigDropDownState.BIG_PALLET -> {
                val bigPallet = remember { bigPallet() }
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(16.dp)
                        .width(244.dp),
                    columns = GridCells.FixedSize(32.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(bigPallet) { color ->
                        Spacer(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(32.dp)
                                .padding(2.dp)
                                .background(
                                    color = color,
                                    shape = CircleShape
                                )
                                .clickable {
                                    onColorClick(color)
                                }
                        )
                    }
                }
            }

            null -> Unit
        }
    }
}

private fun bigPallet(): ImmutableList<Color> {
    val bigPallet = mutableListOf<Color>()
    val pallets = listOf(
        AliveColors.greenPallet,
        AliveColors.pinkPallet,
        AliveColors.orangePallet,
        AliveColors.purplePallet,
        AliveColors.bluePallet,
    )
    pallets.forEach { pallet ->
        bigPallet.add(pallet.dull)
    }
    pallets.forEach { pallet ->
        bigPallet.add(pallet.light)
    }
    pallets.forEach { pallet ->
        bigPallet.add(pallet.original)
    }
    pallets.forEach { pallet ->
        bigPallet.add(pallet.dark)
    }
    pallets.forEach { pallet ->
        bigPallet.add(pallet.veryDark)
    }

    return bigPallet.toImmutableList()
}