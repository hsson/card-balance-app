// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import se.creotec.chscardbalance2.BuildConfig
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.service.BalanceService

class AppFirstRunActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bkgColor = getColor(R.color.color_primary_dark)

        addSlide(AppIntroFragment.newInstance(getString(R.string.first_time_tite), getString(R.string.first_time_desc), R.drawable.happy_face_icon, bkgColor))

        addSlide(AppFirstRunAddCardFragment())

        skipButtonEnabled = false
        setSeparatorColor(bkgColor)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val updateCardIntent = Intent(this, BalanceService::class.java)
        updateCardIntent.action = Constants.ACTION_UPDATE_CARD
        startService(updateCardIntent)
        val preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        preferences.edit().putInt(Constants.PREFS_VERSION_CODE_KEY, BuildConfig.VERSION_CODE).apply()
        goToMain()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        if (oldFragment != null && newFragment != null) {
            // Prevent going back to start page
            if (oldFragment is AppIntroFragment && newFragment is AppFirstRunAddCardFragment) {
                setSwipeLock(true)
            }
        }
    }

    private fun goToMain() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}