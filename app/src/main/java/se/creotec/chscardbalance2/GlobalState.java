// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Locale;

import se.creotec.chscardbalance2.model.CardData;
import se.creotec.chscardbalance2.model.IModel;
import se.creotec.chscardbalance2.model.Model;
import se.creotec.chscardbalance2.service.BalanceService;
import se.creotec.chscardbalance2.util.AlarmScheduler;

public class GlobalState extends Application {

    private IModel model;
    private SharedPreferences preferences;
    private final Gson gson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        this.model = new Model();
        this.preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, MODE_PRIVATE);
        loadCardData();
        loadMenuData();
        scheduleUpdating();
    }

    public IModel getModel() {
        return this.model;
    }

    /**
     * Load data about the food menu from persistent storage
     */
    public void loadMenuData() {
        String lang = this.preferences.getString(Constants.PREFS_MENU_LANGUAGE_KEY, determineSystemLanguage());
        this.model.setPreferredMenuLanguage(lang);
    }

    /**
     * Save data about the food menu to persistent storage
     */
    public synchronized void saveMenuData() {
        this.preferences.edit().putString(Constants.PREFS_MENU_LANGUAGE_KEY, this.model.getPreferredMenuLanguage()).apply();
    }

    /**
     * Load data about the card from persistent storage
     */
    public void loadCardData() {
        String cardJson = this.preferences.getString(Constants.PREFS_CARD_DATA_KEY, "");
        long lastUpdated = this.preferences.getLong(Constants.PREFS_CARD_LAST_UPDATED_KEY, -1);
        this.model.setCardLastTimeUpdated(lastUpdated);

        if (!cardJson.equals("")) {
            CardData data = gson.fromJson(cardJson, CardData.class);
            this.model.setCardData(data);
        } else {
            // TODO: Prompt for card details, maybe
        }
    }

    /**
     * Save data about the card to persistent storage
     */
    public synchronized void saveCardData() {
        CardData data = this.model.getCardData();
        String cardJson = gson.toJson(data, CardData.class);
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(Constants.PREFS_CARD_DATA_KEY, cardJson);
        editor.putLong(Constants.PREFS_CARD_LAST_UPDATED_KEY, this.model.getCardLastTimeUpdate());
        editor.apply();
    }

    /**
     * Determine which language to default to. If the users is running one of
     * the supported languages, use that one.
     *
     * @return One of the supported languages
     */
    private String determineSystemLanguage() {
        String defaultLanguage = Locale.getDefault().getLanguage();
        switch (defaultLanguage) {
            case Constants.ENDPOINT_MENU_LANG_SV:
            case Constants.ENDPOINT_MENU_LANG_EN:
                return defaultLanguage;
            default:
                return Constants.PREFS_MENU_LANGUAGE_DEFAULT;
        }
    }

    private void scheduleUpdating() {
        Intent updateIntent = new Intent(this, BalanceService.class);
        updateIntent.setAction(Constants.ACTION_UPDATE_CARD);

        if (!AlarmScheduler.isAlarmExistingForIntent(this, updateIntent)) {
            AlarmScheduler.scheduleAlarm(this, updateIntent, 0);
        }
    }
}
