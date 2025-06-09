import androidx.compose.runtime.Composable
import config.Theme
import imagesets.ImageLoader

@Composable
fun LgScreenSaver(
    theme: Theme,
    imageLoader: ImageLoader,
) {
    when(theme) {
        Theme.Light -> {
            Light(imageLoader = imageLoader)
        }

        Theme.Dark -> {
            Dark(imageLoader = imageLoader)
        }

        Theme.ActiveRed -> {
            ActiveRed(imageLoader = imageLoader)
        }

        Theme.RoyalBlue -> {
            RoyalBlue(imageLoader = imageLoader)
        }
    }
}