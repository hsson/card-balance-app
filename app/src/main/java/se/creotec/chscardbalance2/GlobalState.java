// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import se.creotec.chscardbalance2.model.CardData;
import se.creotec.chscardbalance2.model.IModel;
import se.creotec.chscardbalance2.model.Model;

public class GlobalState extends Application {

    private IModel model;
    private SharedPreferences preferences;
    private final Gson gson = new Gson();

    private enum RunState {
        NORMAL,
        FIRST,
        UPGRADED
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.model = new Model();
        this.preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, MODE_PRIVATE);
        switch (getRunState()) {
            case NORMAL:
                loadCardData();
                break;
            case FIRST:
                // TODO: Prompt card details
                break;
            case UPGRADED:
                loadCardData();
                break;
        }
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

    public void loadCardData() {
        String cardJson = this.preferences.getString(Constants.PREFS_CARD_DATA_KEY, "");
        if (!cardJson.equals("")) {
            CardData data = gson.fromJson(cardJson, CardData.class);
            this.model.setCardData(data);
        } else {
            // TODO: Prompt for card details, maybe
        }
    }

    public synchronized void saveCardData() {
        CardData data = this.model.getCardData();
        String cardJson = gson.toJson(data, CardData.class);
        this.preferences.edit().putString(Constants.PREFS_CARD_DATA_KEY, cardJson).apply();
    }
}
