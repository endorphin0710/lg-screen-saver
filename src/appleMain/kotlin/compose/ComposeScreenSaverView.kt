@file:OptIn(ExperimentalForeignApi::class)

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import imagesets.ImageLoader
import config.PrefControllerWindow
import config.UserDefaultsPreferences
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.ScreenSaver.ScreenSaverView
import platform.AppKit.NSWindow

class ComposeScreenSaverView(private val imageLoader: ImageLoader): KotlinScreenSaverView() {

    private lateinit var screenSaverView: ScreenSaverView
    private lateinit var composeContentHolder: ComposeContentHolder

    private val userDefaults by lazy { UserDefaultsPreferences }
    private val preferencesController by lazy { PrefControllerWindow(userDefaults) }

    override fun init(screenSaverView: ScreenSaverView, isPreview: Boolean) {
        this.screenSaverView = screenSaverView
        screenSaverView.animationTimeInterval = 1 / 60.0
    }

    override fun startAnimation() {
        super.startAnimation()

        val screenWidth = screenSaverView.frame.useContents { this.size.width }
        val screenHeight = screenSaverView.frame.useContents { this.size.height }
        composeContentHolder = ComposeContentHolder(
            density = requireNotNull(screenSaverView.window).backingScaleFactor.toFloat(),
            size = DpSize(screenWidth.dp, screenHeight.dp)
        )
        screenSaverView.addSubview(composeContentHolder.view)

        val preference = userDefaults.getPreferences()
        composeContentHolder.setContent {
            LgScreenSaver(
                theme = preference.theme,
                imageLoader = imageLoader
            )
        }
    }

//    override val configureSheet: NSWindow? = null
    override val configureSheet: NSWindow? = preferencesController.window
}
