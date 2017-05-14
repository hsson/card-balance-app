// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2;

import android.app.Application;
import android.content.SharedPreferences;

public class GlobalState extends Application {

    private enum RunState {
        NORMAL,
        FIRST,
        UPGRADED
    }

    @Override
    public void onCreate() {
        super.onCreate();

        RunState state = getRunState();
    }

    /**
     * Determines if the application was started normally, was just upgraded, or if it's the
     * first time the app is ran.
     * @return The run state of the application
     */
    private RunState getRunState() {
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, MODE_PRIVATE);
        int savedVersionCode = preferences.getInt(Constants.PREFS_VERSION_CODE_KEY, Constants.PREFS_VERSION_CODE_NONEXISTING);
        preferences.edit().putInt(Constants.PREFS_VERSION_CODE_KEY, currentVersionCode).apply();

        if (savedVersionCode == Constants.PREFS_VERSION_CODE_NONEXISTING) {
            return RunState.FIRST;
        } else if (currentVersionCode > savedVersionCode) {
            return RunState.UPGRADED;
        } else {
            return RunState.NORMAL;
        }
    }
}
