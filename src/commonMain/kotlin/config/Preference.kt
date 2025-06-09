package config

interface PreferenceStorage {
    fun getPreferences(): Preferences
    fun update(actions: Preferences.() -> Unit)
}

data class Preferences(
    var theme: Theme
)

enum class Theme {
    Light,
    Dark,
    ActiveRed,
    RoyalBlue,
}