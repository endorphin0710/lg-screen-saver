import compose.ComposeAssetImageLoader
import platform.AppKit.NSWindow
import platform.Foundation.NSBundle
import platform.ScreenSaver.ScreenSaverView

fun create(): KotlinScreenSaverView = ComposeScreenSaverView(imageLoader = ComposeAssetImageLoader())

abstract class KotlinScreenSaverView {

    protected lateinit var view: ScreenSaverView
        private set

    protected lateinit var bundle: NSBundle
        private set

    protected var isPreview = false
        private set

    open fun init(screenSaverView: ScreenSaverView, isPreview: Boolean) {
        this.view = screenSaverView
        this.bundle = NSBundle.bundleWithIdentifier("com.example.LgScreenSaver")!!
        this.isPreview = isPreview
    }

    open fun animateOneFrame() {}

    open fun startAnimation() {}

    open val configureSheet: NSWindow? = null
}
