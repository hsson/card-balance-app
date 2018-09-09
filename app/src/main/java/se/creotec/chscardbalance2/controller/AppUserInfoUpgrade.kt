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

class AppUserInfoUpgrade : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bkgColor = getColor(R.color.color_primary_dark)

        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_login_sorry_title), getString(R.string.slide_login_sorry_desc), R.drawable.sad_face_icon, bkgColor))
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_login_back_title), getString(R.string.slide_login_back_desc), R.drawable.happy_face_icon, bkgColor))
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide_login_title), getString(R.string.slide_login_desc), R.drawable.img_slide_card, bkgColor))

        skipButtonEnabled = false
        setSeparatorColor(bkgColor)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val preferences = getSharedPreferences(Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        preferences.edit().putInt(Constants.PREFS_VERSION_CODE_KEY, BuildConfig.VERSION_CODE).apply()
        goToMain()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToMain()
    }

    private fun goToMain() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}