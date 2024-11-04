package ru.notexe.alive.domain

internal data class Frame(
    val id: Long,
    val paintObjects: List<PaintObject>,
)

internal data class ColorProperties(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float,
)

internal data class PaintObject(
    val startX: Float,
    val startY: Float,
    val changes: List<Change>,
    val colorProperties: ColorProperties,
    val lineStrokeWidth: Float,
    val paintingMode: PaintMode,
)

internal data class Change(
    val dragAmountX: Float,
    val dragAmountY: Float,
)

enum class PaintMode {
    PENCIL,
    BRUSH,
    ERASE,
    SHAPE,
}