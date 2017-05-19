// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import se.creotec.chscardbalance2.BuildConfig
import se.creotec.chscardbalance2.Constants

class SplashActivity : AppCompatActivity() {

    private enum class RunState {
        NORMAL,
        FIRST,
        UPGRADED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (runState) {
            SplashActivity.RunState.NORMAL ->
                // Do nothing special
                startMain()
            SplashActivity.RunState.FIRST ->
                // TODO: Prompt card details
                startMain()
            SplashActivity.RunState.UPGRADED ->
                // Do something special, maybe
                startMain()
        }
    }

    private fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

     // Determines if the application was started normally, was just upgraded, or if it's the
     // first time the app is ran.
    private val runState: RunState
        get() {
            val currentVersionCode = BuildConfig.VERSION_CODE
            val preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
            val savedVersionCode = preferences.getInt(Constants.PREFS_VERSION_CODE_KEY, Constants.PREFS_VERSION_CODE_NONEXISTING)
            preferences.edit().putInt(Constants.PREFS_VERSION_CODE_KEY, currentVersionCode).apply()

            if (savedVersionCode == Constants.PREFS_VERSION_CODE_NONEXISTING) {
                return RunState.FIRST
            } else if (currentVersionCode > savedVersionCode) {
                return RunState.UPGRADED
            } else {
                return RunState.NORMAL
            }
        }
}
