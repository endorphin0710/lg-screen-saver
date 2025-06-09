package imagesets

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter

data class LoadedImage (
    val image: Painter,
    val width: Float,
    val height: Float,
)

interface ImageLoader {
    fun loadImage(
        name: String,
    ): LoadedImage?
}

/**
 * Convert image aspect ratio to target width and height values.
 */
typealias Adjuster = (Float) -> Pair<Float, Float>