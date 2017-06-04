// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2

object Constants {
    // Set to true to show upgrade screen. Useful when making big changes.
    // Don't forget to set to false again for next release.
    val VERSION_SHOULD_SHOW_UPGRADE_INTRO = false

    val ENDPOINT_BALANCE = "/balance"
    val ENDPOINT_MENU = "/menu"
    val ENDPOINT_CHARGE = "/charge"
    val ENDPOINT_MENU_LANG_EN = "en"
    val ENDPOINT_MENU_LANG_SV = "sv"
    val ENDPOINT_TIMEOUT: Int = 10 * 1000 // Milliseconds

    val CARD_NUMBER_LENGTH: Int = 16
    val CARD_CURRENCY_SUFFIX = "SEK"

    val ACTION_UPDATE_CARD = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_CARD"
    val ACTION_UPDATE_MENU = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_MENU"
    val ACTION_COPY_CARD_NUMBER = BuildConfig.APPLICATION_ID + ".ACTION_COPY_CARD_NUMBER"
    val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"

    val EXTRAS_CARD_NUMBER_KEY = BuildConfig.APPLICATION_ID + ".EXTRAS_CARD_NUMBER_KEY"

    val PREFS_FILE_NAME = "PreferenceConfig"
    val PREFS_VERSION_CODE_KEY = "version_code"
    val PREFS_VERSION_CODE_NONEXISTING = -1
    val PREFS_CARD_DATA_KEY = "card_data"
    val PREFS_CARD_LAST_UPDATED_KEY = "card_last_updated"
    val PREFS_MENU_LANGUAGE_KEY = "menu_preferred_language"
    val PREFS_MENU_LANGUAGE_DEFAULT = ENDPOINT_MENU_LANG_EN
    val PREFS_MENU_DATA_KEY = "menu_data"
    val PREFS_NOTIFICATIONS_DATA_KEY = "notifications_data"
    val PREFS_NOTIFICATION_LOW_BALANCE_ENABLED_DEFAULT = true
    val PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_DEFAULT: Int = 50
    val PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_MIN: Int = 10
    val PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_MAX: Int = 200
    val PREFS_MENU_LAST_UPDATED_KEY = "menu_last_updated"
    val PREFS_CARD_NUMBER_LEGACY_KEY = "se.creotec.chscardbalance.PREFS_CARD_NUMBER"
    val PREFS_FILE_NAME_LEGACY = "se.creotec.chscardbalance.SHARED_PREFS_NAME"

    val INTENT_RESTAURANT_DATA_KEY = BuildConfig.APPLICATION_ID + ".RESTAURANT_DATA"

    val GITHUB_URL = "https://github.com/hsson/card-balance-app/"
}
