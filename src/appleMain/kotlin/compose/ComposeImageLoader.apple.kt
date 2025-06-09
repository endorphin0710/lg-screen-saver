package compose

import androidx.compose.ui.graphics.painter.BitmapPainter
import imagesets.ImageLoader
import imagesets.LoadedImage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import platform.AppKit.NSDataAsset
import platform.Foundation.NSBundle

val bundle by lazy {
    requireNotNull(NSBundle.bundleWithIdentifier("com.example.LgScreenSaver"))
}

@OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
class ComposeAssetImageLoader() : ImageLoader {

    override fun loadImage(name: String): LoadedImage {
        val data = NSDataAsset(name = name, bundle).data
        val nativeBytes = requireNotNull(data.bytes)
        val bytes = nativeBytes.readBytes(data.length.toInt())
        val painter = BitmapPainter(bytes.decodeToImageBitmap())
        return LoadedImage(
            image = painter,
            width = painter.intrinsicSize.width,
            height = painter.intrinsicSize.height,
        )
    }
}