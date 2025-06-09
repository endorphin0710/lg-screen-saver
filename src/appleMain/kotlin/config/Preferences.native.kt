package config

import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val APP_ID = "com.example.LgScreenSaver"

object UserDefaultsPreferences : PreferenceStorage {
    private var THEME by LongUserDefaultDelegate(0)

    fun reset() {
        setPreference(
            Preferences(theme = Theme.Light)
        )
    }

    override fun getPreferences(): Preferences {
        return Preferences(
            theme = Theme.entries[THEME],
        )
    }

    private fun setPreference(preferences: Preferences) {
        THEME = preferences.theme.ordinal
    }

    override fun update(actions: Preferences.() -> Unit) {
        setPreference(getPreferences().apply(actions))
    }
}

private class LongUserDefaultDelegate(private val default: Long) : ReadWriteProperty<Any?, Int> {
    private val userDefaults = NSUserDefaults()

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return ((userDefaults.objectForKey(property.name) as? Long) ?: default).toInt()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        userDefaults.setInteger(value.toLong(), forKey = property.name)
    }
}

private class StringUserDefaultDelegate : ReadWriteProperty<Any?, String> {
    private val userDefaults = NSUserDefaults()

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return (userDefaults.objectForKey(property.name) as? String) ?: ""
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        userDefaults.setValue(value, forKey = property.name)
    }
}