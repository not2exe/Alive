package ru.notexe.alive.domain

import kotlinx.serialization.Serializable

internal data class Frame(
    val id: Long,
    val paintObjects: List<PaintObject>,
) {

}

internal data class ColorProperties(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float,
)

internal data class PaintObject(
    val lines: List<Line>,
    val colorProperties: ColorProperties,
    val lineStrokeWidth: Float,
    val paintingMode: PaintMode,
)

@Serializable
internal data class Line(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
)

enum class PaintMode {
    PENCIL,
    BRUSH,
    ERASE,
    SHAPE,
}