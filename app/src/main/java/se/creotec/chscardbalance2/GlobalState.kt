// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import se.creotec.chscardbalance2.model.CardData
import se.creotec.chscardbalance2.model.IModel
import se.creotec.chscardbalance2.model.MenuData
import se.creotec.chscardbalance2.model.Model
import se.creotec.chscardbalance2.service.BalanceService
import se.creotec.chscardbalance2.util.AlarmScheduler
import java.util.*

class GlobalState : Application() {

    var model: IModel = Model()
        private set
    private var preferences: SharedPreferences? = null
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        loadCardData()
        loadMenuData()
        scheduleUpdating()
        setupImageLoader()
    }

    /**
     * Load data about the food menu from persistent storage
     */
    fun loadMenuData() {
        val lang = preferences!!.getString(Constants.PREFS_MENU_LANGUAGE_KEY, determineSystemLanguage())
        val menuLastUpdated = preferences!!.getLong(Constants.PREFS_MENU_LAST_UPDATED_KEY, -1)
        val menuDataJSON = preferences!!.getString(Constants.PREFS_MENU_DATA_KEY, "")

        if (menuDataJSON != "") {
            val menu = gson.fromJson(menuDataJSON, MenuData::class.java)
            model.menuData = menu
        }

        model.preferredMenuLanguage = lang
        model.menuLastTimeUpdated = menuLastUpdated

    }

    /**
     * Save data about the food menu to persistent storage
     */
    @Synchronized fun saveMenuData() {
        val menu = model.menuData
        val menuDataJSON = gson.toJson(menu, MenuData::class.java)

        val editor = preferences!!.edit()
        editor.putString(Constants.PREFS_MENU_LANGUAGE_KEY, model.preferredMenuLanguage)
        editor.putString(Constants.PREFS_MENU_DATA_KEY, menuDataJSON)
        editor.putLong(Constants.PREFS_MENU_LAST_UPDATED_KEY, model.menuLastTimeUpdated)
        editor.apply()
    }

    /**
     * Load data about the card from persistent storage
     */
    fun loadCardData() {
        val cardJson = preferences!!.getString(Constants.PREFS_CARD_DATA_KEY, "")
        val lastUpdated = preferences!!.getLong(Constants.PREFS_CARD_LAST_UPDATED_KEY, -1)
        model.cardLastTimeUpdated = lastUpdated

        if (cardJson != "") {
            val data = gson.fromJson(cardJson, CardData::class.java)
            model.cardData = data
        } else {
            // TODO: Prompt for card details, maybe
        }
    }

    /**
     * Save data about the card to persistent storage
     */
    @Synchronized fun saveCardData() {
        val data = model.cardData
        val cardJson = gson.toJson(data, CardData::class.java)

        val editor = preferences!!.edit()
        editor.putString(Constants.PREFS_CARD_DATA_KEY, cardJson)
        editor.putLong(Constants.PREFS_CARD_LAST_UPDATED_KEY, model.cardLastTimeUpdated)
        editor.apply()
    }

     // Determine which language to default to. If the users is running one of
     // the supported languages, use that one.
    private fun determineSystemLanguage(): String {
        val defaultLanguage = Locale.getDefault().language
        when (defaultLanguage) {
            Constants.ENDPOINT_MENU_LANG_SV -> return defaultLanguage
            Constants.ENDPOINT_MENU_LANG_EN -> return defaultLanguage
            else -> return Constants.PREFS_MENU_LANGUAGE_DEFAULT
        }
    }

    // Sets up scheduling of balance updating
    private fun scheduleUpdating() {
        val updateIntent = Intent(this, BalanceService::class.java)
        updateIntent.action = Constants.ACTION_UPDATE_CARD

        if (!AlarmScheduler.isAlarmExistingForIntent(this, updateIntent)) {
            AlarmScheduler.scheduleAlarm(this, updateIntent, 0)
        }
    }

    // Configure the image loader
    private fun setupImageLoader() {
        val defaultOptions = DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.default_food)
                .showImageOnLoading(R.drawable.default_food)
                .showImageForEmptyUri(R.drawable.default_food)
                .build()

        val config = ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build()
        ImageLoader.getInstance().init(config)
    }
}
