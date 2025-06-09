import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.zIndex
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ColorFilter
import component.LgLogo
import design.*
import imagesets.ImageLoader

@Composable
fun Dark(
    imageLoader: ImageLoader,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Dark)
            .padding(16.dp),
    ) {
        val left = imageLoader.loadImage("slogan_left_black")
        val right = imageLoader.loadImage("slogan_right_black")
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5F)
                .height(240.dp)
                .padding(end = 136.dp)
                .align(Alignment.CenterStart)
                .zIndex(1F)
        ) {
            if (left != null) {
                Image(
                    painter = left.image,
                    contentDescription = "Life's",
                    contentScale = ContentScale.FillHeight,
                    colorFilter = ColorFilter.tint(White),
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5F)
                .height(240.dp)
                .padding(start = 136.dp)
                .align(Alignment.CenterEnd)
                .zIndex(-1F)
        ) {
            if (right != null) {
                Image(
                    painter = right.image,
                    contentDescription = "Good",
                    contentScale = ContentScale.FillHeight,
                    colorFilter = ColorFilter.tint(White),
                    modifier = Modifier.align(Alignment.CenterStart),
                )
            }
        }
        LgLogo(
            modifier = Modifier
                .size(320.dp)
                .background(
                    color = HeritageRed,
                    shape = CircleShape
                )
                .padding(30.dp)
                .align(Alignment.Center)
                .zIndex(0F),
            color = White,
        )
    }
}
