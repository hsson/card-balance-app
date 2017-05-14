// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2;

public final class Constants {
    private Constants() {}

    public static final String ENDPOINT_BALANCE = "/balance";
    public static final String ENDPOINT_MENU = "/menu";
    public static final String ENDPOINT_MENU_LANG_EN = "en";
    public static final String ENDPOINT_MENU_LANG_SV = "sv";
    public static final int ENDPOINT_TIMEOUT = 10 * 1000; // Miliseconds

    public static final int CARD_NUMBER_LENGTH = 16;

    public static final String ACTION_UPDATE_CARD = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_CARD";
    public static final String ACTION_UPDATE_MENU = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_MENU";
    public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    public static final String PREFS_FILE_NAME = "PreferenceConfig";
    public static final String PREFS_VERSION_CODE_KEY = "version_code";
    public static final int PREFS_VERSION_CODE_NONEXISTING = -1;
    public static final String PREFS_CARD_DATA_KEY = "card_data";
    public static final String PREFS_CARD_LAST_UPDATED_KEY = "card_last_updated";
    public static final String PREFS_MENU_LANGUAGE_KEY = "menu_preferred_language";
    public static final String PREFS_MENU_LANGUAGE_DEFAULT = ENDPOINT_MENU_LANG_EN;

}
