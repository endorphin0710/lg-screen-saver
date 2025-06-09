package config

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.copy
import platform.AppKit.NSBackingStoreBuffered
import platform.AppKit.NSBezelStyleRounded
import platform.AppKit.NSButton
import platform.AppKit.NSComboBox
import platform.AppKit.NSControl
import platform.AppKit.NSControlStateValueOff
import platform.AppKit.NSControlStateValueOn
import platform.AppKit.NSLayoutConstraint
import platform.AppKit.NSOpenPanel
import platform.AppKit.NSStackView
import platform.AppKit.NSStackViewGravityCenter
import platform.AppKit.NSStackViewGravityTop
import platform.AppKit.NSStackViewGravityTrailing
import platform.AppKit.NSStepper
import platform.AppKit.NSTextField
import platform.AppKit.NSUserInterfaceLayoutOrientationVertical
import platform.AppKit.NSWindow
import platform.AppKit.NSWindowController
import platform.AppKit.NSWindowDelegateProtocol
import platform.AppKit.NSWindowStyleMaskClosable
import platform.AppKit.addView
import platform.AppKit.heightAnchor
import platform.AppKit.translatesAutoresizingMaskIntoConstraints
import platform.AppKit.widthAnchor
import platform.Foundation.NSMakeRect
import platform.Foundation.NSSelectorFromString
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty0

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class PrefControllerWindow(private val prefs: UserDefaultsPreferences) : NSWindowController(
    NSWindow(
        contentRect = NSMakeRect(x = 0.0, y = 0.0, w = 320.0, h = 120.0),
        styleMask = NSWindowStyleMaskClosable,
        backing = NSBackingStoreBuffered,
        defer = true
    )
), NSWindowDelegateProtocol {

    private lateinit var themeComboBox: NSComboBox

    init {
        val mainStack = NSStackView()
        mainStack.orientation = NSUserInterfaceLayoutOrientationVertical

        val themeStack = createComboBox(title = "Theme", ::themeComboBox)
        themeComboBox.removeAllItems()
        themeComboBox.addItemsWithObjectValues(listOf("Light", "Dark", "Active Red", "Royal Blue"))
        mainStack.addView(themeStack, NSStackViewGravityTop)

        mainStack.addView(createButtonStack(), NSStackViewGravityTrailing)
        mainStack.setEdgeInsets(
            mainStack.edgeInsets.copy {
                top = 16.0
                bottom = 16.0
            }
        )

        requireNotNull(window).contentView = mainStack

        loadValuesFromPrefs()
    }

    private val openPanel by lazy {
        NSOpenPanel().apply {
            allowsMultipleSelection = false
            canChooseDirectories = true
            canCreateDirectories = false
            canChooseFiles = false
        }
    }

    private fun createComboBox(
        title: String,
        comboBoxProp: KMutableProperty0<NSComboBox>,
    ): NSStackView {
        val stack = NSStackView()
        stack.translatesAutoresizingMaskIntoConstraints = false

        val label = NSTextField().apply {
            stringValue = title
            editable = false
            selectable = false
            drawsBackground = false
            bezeled = false
        }

        val comboBox = NSComboBox().apply {
            editable = false
        }
        comboBoxProp.set(comboBox)

        stack.addView(label, NSStackViewGravityCenter)
        stack.addView(comboBox, NSStackViewGravityCenter)

        NSLayoutConstraint.activateConstraints(
            listOf(comboBox.widthAnchor.constraintEqualToConstant(120.0))
        )

        return stack
    }

    private fun createButtonStack(): NSStackView {
        val buttonsStack = NSStackView()

        val cancelButton = createButton("Cancel", "Cancel", ::performCancel)
        buttonsStack.addView(cancelButton, NSStackViewGravityTrailing)

        val okButton = createButton(title = "Confirm", keyEquivalent = "Confirm", action = ::performOk)
        buttonsStack.addView(okButton, NSStackViewGravityTrailing)

        NSLayoutConstraint.activateConstraints(
            listOf(
                okButton.widthAnchor.constraintEqualToConstant(75.0),
                okButton.heightAnchor.constraintEqualToConstant(25.0),
                cancelButton.widthAnchor.constraintEqualToConstant(75.0),
                cancelButton.heightAnchor.constraintEqualToConstant(25.0),
            )
        )

        return buttonsStack
    }

    private fun createButton(title: String, keyEquivalent: String, action: KFunction<Unit>): NSButton {
        return NSButton().apply {
            setListener(action)
            this.title = title
            this.keyEquivalent = keyEquivalent
            this.bezelStyle = NSBezelStyleRounded
        }
    }

    private fun NSControl.setListener(selfFunc: KFunction<Unit>) {
        target = this@PrefControllerWindow
        action = NSSelectorFromString(selfFunc.name)
    }

    private fun loadValuesFromPrefs() {
        val prefs = prefs.getPreferences()
        themeComboBox.selectItemAtIndex(prefs.theme.ordinal.toLong())
    }

    private fun saveValuesToPrefs() {
        prefs.update {
            theme = when (themeComboBox.indexOfSelectedItem.toInt()) {
                0 -> Theme.Light
                1 -> Theme.Dark
                2 -> Theme.ActiveRed
                3 -> Theme.RoyalBlue
                else -> Theme.Light
            }
        }
    }

    @ObjCAction
    fun performCancel() {
        window?.let { it.sheetParent?.endSheet(it) }
    }

    @ObjCAction
    fun performOk() {
        saveValuesToPrefs()
        window?.let { it.sheetParent?.endSheet(it) }
    }
}