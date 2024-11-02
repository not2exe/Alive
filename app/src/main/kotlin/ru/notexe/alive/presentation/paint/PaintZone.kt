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
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.notexe.alive.R
import ru.notexe.alive.presentation.applyIf
import ru.notexe.alive.presentation.contract.Line
import ru.notexe.alive.presentation.contract.PaintObject
import ru.notexe.alive.presentation.contract.PaintingSettings
import ru.notexe.alive.ui.theme.AliveTheme

@Composable
internal fun ColumnScope.PaintZone(
    paintingSettings: PaintingSettings,
    framePaintObjects: ImmutableList<PaintObject>,
    currentAnimationFrame: ImmutableList<PaintObject>?,
    onNewPaintObjectAdded: (ImmutableList<Line>) -> Unit,
) {
    val currentPaintObject = remember { mutableStateListOf<Line>() }

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
                .applyIf(
                    condition = currentAnimationFrame == null,
                ) {
                    pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                onNewPaintObjectAdded(
                                    currentPaintObject.toImmutableList(),
                                )
                                currentPaintObject.clear()
                            },
                            onDrag = { change, dragAmount ->
                                currentPaintObject.add(
                                    Line(
                                        start = change.position - dragAmount,
                                        end = change.position,
                                    )
                                )
                            }
                        )
                    }
                }
                .drawWithCache {
                    val paint = Paint()
                    onDrawBehind {
                        drawFrame(
                            paint = paint,
                            frameToDraw = currentAnimationFrame ?: framePaintObjects,
                            paintingSettings = paintingSettings,
                            currentPaintObject = currentPaintObject
                        )
                    }
                }
        )
    }
}

private fun DrawScope.drawFrame(
    paint: Paint,
    frameToDraw: ImmutableList<PaintObject>,
    paintingSettings: PaintingSettings,
    currentPaintObject: List<Line>,
) {
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.apply {
            val saveLayer = saveLayer(null, null)
            painObject(
                canvas = canvas,
                paint = paint,
                paintObjects = frameToDraw,
            )
            paint.apply {
                color = paintingSettings.currentColor
                strokeWidth = paintingSettings.strokeWidth.toPx()
                strokeCap = paintingSettings.paintingMode.strokeCap
                blendMode = paintingSettings.paintingMode.blendMode
            }
            currentPaintObject.forEach { line ->
                canvas.drawLine(
                    p1 = line.start,
                    p2 = line.end,
                    paint = paint,
                )
            }
            restoreToCount(saveLayer)
        }
    }
}

private fun DrawScope.painObject(
    canvas: Canvas,
    paint: Paint,
    paintObjects: ImmutableList<PaintObject>
) {
    paintObjects.forEach { paintObject ->
        paint.apply {
            strokeWidth = paintObject.strokeWidth.toPx()
            strokeCap = paintObject.paintingMode.strokeCap
            blendMode = paintObject.paintingMode.blendMode
            color = paintObject.color
        }
        paintObject.lines.forEach { line ->
            canvas.drawLine(
                p1 = line.start,
                p2 = line.end,
                paint = paint,
            )
        }
    }
}