import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

val Int.dpInt: Int
    @Composable
    get() = with(LocalDensity.current) {
        this@dpInt.toDp().value.toInt()
    }

val Float.dpInt: Int
    @Composable
    get() = with(LocalDensity.current) {
        this@dpInt.toDp().value.toInt()
    }

val Int.pxInt: Int
    @Composable
    get() = with(LocalDensity.current) {
        this@pxInt.dp.toPx().toInt()
    }

val Int.pxFloat: Float
    @Composable
    get() = with(LocalDensity.current) {
        this@pxFloat.dp.toPx()
    }