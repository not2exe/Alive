package ru.notexe.alive.presentation.main.paint

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.copy
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
import ru.notexe.alive.presentation.main.contract.ChangePresentation
import ru.notexe.alive.presentation.main.contract.PaintObjectPresentation
import ru.notexe.alive.presentation.main.contract.PaintingSettings
import ru.notexe.alive.theme.AliveTheme


@Composable
internal fun ColumnScope.PaintZone(
    paintingSettings: PaintingSettings,
    currentFrame: ImmutableList<PaintObjectPresentation>,
    previousFrame: ImmutableList<PaintObjectPresentation>,
    isAnimationPlaying: Boolean,
    currentAnimationFrame: ImmutableList<PaintObjectPresentation>?,
    onNewPaintObjectAdded: (start: Offset, changes: ImmutableList<ChangePresentation>, path: Path) -> Unit,
) {
    val currentChanges = remember { mutableStateListOf<ChangePresentation>() }
    val start = remember { mutableStateOf(Offset.Zero) }
    val path = remember { mutableStateOf(Path()) }

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
                    condition = !isAnimationPlaying,
                ) {
                    pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset: Offset ->
                                start.value = offset
                                path.value.moveTo(offset.x, offset.y)
                            },
                            onDragEnd = {
                                onNewPaintObjectAdded(
                                    start.value,
                                    currentChanges.toImmutableList(),
                                    path.value.copy()
                                )
                                currentChanges.clear()
                                path.value = Path()
                            },
                            onDrag = { _, dragAmount ->
                                path.value.relativeLineTo(dragAmount.x, dragAmount.y)
                                currentChanges.add(
                                    ChangePresentation(
                                        dragAmount = dragAmount,
                                    )
                                )
                            }
                        )
                    }
                }
                .drawWithCache {
                    val paint = Paint()
                    onDrawBehind {
                        drawPaintZone(
                            paint = paint,
                            currentFrame = currentAnimationFrame ?: currentFrame,
                            paintingSettings = paintingSettings,
                            currentPaintObject = currentChanges,
                            previousFrame = if (!isAnimationPlaying) previousFrame else null,
                            changes = currentChanges,
                            currentPath = path.value,
                        )
                    }
                }
        )
    }
}

private fun DrawScope.drawPaintZone(
    paint: Paint,
    currentFrame: ImmutableList<PaintObjectPresentation>,
    previousFrame: ImmutableList<PaintObjectPresentation>?,
    paintingSettings: PaintingSettings,
    currentPaintObject: List<ChangePresentation>,
    currentPath: Path,
    changes: SnapshotStateList<ChangePresentation>,
) {
    drawIntoCanvas { canvas ->
        println(changes)
        canvas.nativeCanvas.apply {
            val saveLayer = saveLayer(null, null)
            previousFrame?.let {
                println(it)
                drawFrame(
                    canvas = canvas,
                    paint = paint,
                    paintObjects = previousFrame,
                    alpha = 0.5f,
                )
            }
            drawFrame(
                canvas = canvas,
                paint = paint,
                paintObjects = currentFrame,
            )
            paint.apply {
                color = paintingSettings.currentColor
                strokeWidth = paintingSettings.strokeWidth.toPx()
                strokeCap = paintingSettings.paintingMode.strokeCap
                blendMode = paintingSettings.paintingMode.blendMode
                style = PaintingStyle.Stroke
            }
            // This is needed to retrigger draw when path is changing
            currentPaintObject.forEach {}
            canvas.drawPath(
                path = currentPath,
                paint = paint,
            )
            restoreToCount(saveLayer)
        }
    }
}

private fun DrawScope.drawFrame(
    canvas: Canvas,
    paint: Paint,
    paintObjects: ImmutableList<PaintObjectPresentation>,
    alpha: Float = 1f,
) {
    paintObjects.forEach { paintObject ->
        paint.apply {
            strokeWidth = paintObject.strokeWidth.toPx()
            strokeCap = paintObject.paintingMode.strokeCap
            blendMode = paintObject.paintingMode.blendMode
            color = paintObject.color
            this.alpha = alpha
            style = PaintingStyle.Stroke
        }
        canvas.drawPath(
            path = paintObject.path,
            paint = paint,
        )
    }
}