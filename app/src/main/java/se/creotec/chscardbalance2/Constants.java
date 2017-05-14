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

    public static final String ACTION_UPDATE_BALANCE = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_BALANCE";
    public static final String ACTION_UPDATE_MENU = BuildConfig.APPLICATION_ID + ".ACTION_UPDATE_MENU";
}
