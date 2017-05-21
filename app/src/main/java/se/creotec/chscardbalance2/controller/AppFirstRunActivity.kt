// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import se.creotec.chscardbalance2.R

class AppFirstRunActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bkgColor = getColor(R.color.color_primary_dark)

        // TODO: Smiley face image
        addSlide(AppIntroFragment.newInstance("Welcome", "This app lets you quickly charge your card. But first, you must add your card number", R.mipmap.ic_launcher, bkgColor))

        addSlide(AppFirstRunAddCardFragment.newInstance())

        skipButtonEnabled = false
        setSeparatorColor(bkgColor)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToMain()
    }

    private fun goToMain() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}