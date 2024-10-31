package ru.notexe.alive.presentation.paint

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import kotlinx.collections.immutable.ImmutableList
import ru.notexe.alive.Line
import ru.notexe.alive.R
import ru.notexe.alive.ui.theme.AliveTheme

@Composable
internal fun ColumnScope.PaintZone(
    currentAnimationFrame: ImmutableList<Line>?,
    color: Color,
    strokeWidth: Dp,
) {
    val currentEditingFrame = remember {
        mutableStateListOf<Line>()
    }

    Box(
        modifier = Modifier
            .weight(1f, false)
            .fillMaxWidth()
            .clip(AliveTheme.shapes.roundedCornerShape20)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.white_paper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        currentEditingFrame.add(
                            Line(
                                start = change.position - dragAmount,
                                end = change.position,
                            )
                        )
                    }
                }
                .drawWithCache {
                    val paint = Paint().apply {
                        this.color = color
                        this.strokeWidth = strokeWidth.toPx()
                        strokeCap = StrokeCap.Round
                    }

                    onDrawBehind {
                        drawIntoCanvas { canvas ->
                            (currentAnimationFrame ?: currentEditingFrame).forEach { line ->
                                canvas.drawLine(
                                    p1 = line.start,
                                    p2 = line.end,
                                    paint = paint
                                )
                            }
                        }
                    }
                }
        )
    }
}