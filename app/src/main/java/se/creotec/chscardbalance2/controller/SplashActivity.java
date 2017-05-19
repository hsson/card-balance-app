// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import se.creotec.chscardbalance2.BuildConfig;
import se.creotec.chscardbalance2.Constants;

public class SplashActivity extends AppCompatActivity {

    private enum RunState {
        NORMAL,
        FIRST,
        UPGRADED
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (getRunState()) {
            case NORMAL:
                // Do nothing special
                startMain();
                break;
            case FIRST:
                // TODO: Prompt card details
                startMain();
                break;
            case UPGRADED:
                // Do something special, maybe
                startMain();
                break;
        }
    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Determines if the application was started normally, was just upgraded, or if it's the
     * first time the app is ran.
     * @return The run state of the application
     */
    private RunState getRunState() {
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences preferences = getSharedPreferences(Constants.INSTANCE.getPREFS_FILE_NAME(), MODE_PRIVATE);
        int savedVersionCode = preferences.getInt(Constants.INSTANCE.getPREFS_VERSION_CODE_KEY(), Constants.INSTANCE.getPREFS_VERSION_CODE_NONEXISTING());
        preferences.edit().putInt(Constants.INSTANCE.getPREFS_VERSION_CODE_KEY(), currentVersionCode).apply();

        if (savedVersionCode == Constants.INSTANCE.getPREFS_VERSION_CODE_NONEXISTING()) {
            return RunState.FIRST;
        } else if (currentVersionCode > savedVersionCode) {
            return RunState.UPGRADED;
        } else {
            return RunState.NORMAL;
        }
    }
}
