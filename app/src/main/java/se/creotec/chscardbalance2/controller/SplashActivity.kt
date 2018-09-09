// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import se.creotec.chscardbalance2.BuildConfig
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.model.CardData
import se.creotec.chscardbalance2.service.BalanceService

class SplashActivity : AppCompatActivity() {

    private enum class RunState {
        NORMAL,
        FIRST,
        UPGRADED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (runState) {
            SplashActivity.RunState.NORMAL -> {
                // Do nothing special
                completeUpgrade()
                startMain()
            }
            SplashActivity.RunState.FIRST -> {
                // Check for any legacy data from version 2.x.x
                val legacyNumber = getLegacyNumber()
                if (legacyNumber != null && legacyNumber != "") {
                    convertLegacyNumber(legacyNumber)

                    // Introduce old user to new interface
                    val intent = Intent(this, AppUpgradedFromLegacyActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val firstRunIntent = Intent(this, AppFirstRunActivity::class.java)
                    startActivity(firstRunIntent)
                    finish()
                }
            }
            SplashActivity.RunState.UPGRADED -> {
                val savedVersionCode = getSavedVersionCode()
                if (savedVersionCode <= 44) {
                    // Upgraded to using the userInfo cookie in balance
                    // TODO: Show special slide and save new code
                } else {
                    completeUpgrade()
                    startMain()
                }
            }
        }
    }

    private fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Get eventual legacy number from saved preferences
    private fun getLegacyNumber(): String? {
        val preferences = getSharedPreferences(Constants.PREFS_FILE_NAME_LEGACY, Context.MODE_PRIVATE)
        return preferences.getString(Constants.PREFS_CARD_NUMBER_LEGACY_KEY, null)
    }

    // Convert an old legacy number to new model
    private fun convertLegacyNumber(number: String) {
        val global = application as GlobalState
        val cardData = CardData()
        cardData.cardNumber = number
        global.model.cardData = cardData
        global.saveCardData()

        // Delete legacy number
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.deleteSharedPreferences(Constants.PREFS_FILE_NAME_LEGACY)
        } else {
            val editor = getSharedPreferences(Constants.PREFS_FILE_NAME_LEGACY, Context.MODE_PRIVATE).edit()
            editor.putString(Constants.PREFS_CARD_NUMBER_LEGACY_KEY, null)
            editor.apply()
        }

        // Attempt to update card info from backend
        val updateBalanceIntent = Intent(this, BalanceService::class.java)
        updateBalanceIntent.action = Constants.ACTION_UPDATE_CARD
        this.startService(updateBalanceIntent)
    }

    private fun getSavedVersionCode(): Int {
        val preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(Constants.PREFS_VERSION_CODE_KEY, Constants.PREFS_VERSION_CODE_NONEXISTING)
    }

    // Determines if the application was started normally, was just upgraded, or if it's the
    // first time the app is ran.
    private val runState: RunState
        get() {
            val currentVersionCode = BuildConfig.VERSION_CODE
            val savedVersionCode = getSavedVersionCode()

            return when {
                savedVersionCode == Constants.PREFS_VERSION_CODE_NONEXISTING -> RunState.FIRST
                currentVersionCode > savedVersionCode -> RunState.UPGRADED
                else -> RunState.NORMAL
            }
        }

    private fun completeUpgrade() {
        val preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        preferences.edit().putInt(Constants.PREFS_VERSION_CODE_KEY, BuildConfig.VERSION_CODE).apply()
    }
}
