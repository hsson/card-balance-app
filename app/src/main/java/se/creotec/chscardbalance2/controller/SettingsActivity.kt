package se.creotec.chscardbalance2.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R

class SettingsActivity : AppCompatActivity() {

    var cardNumberContainer: View? = null
    var cardNumberText: TextView? = null

    var menuLangContainer: View? = null
    var menuLangText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        cardNumberContainer = findViewById(R.id.settings_card_number)
        cardNumberText = findViewById(R.id.settings_card_number_text) as TextView

        menuLangContainer = findViewById(R.id.settings_menu_lang)
        menuLangText = findViewById(R.id.settings_menu_lang_text) as TextView



        val global = application as GlobalState
        cardNumberText?.text = global.model.cardData.cardNumber
        when (global.model.preferredMenuLanguage) {
            Constants.ENDPOINT_MENU_LANG_EN -> menuLangText?.text = getString(R.string.prefs_menu_lang_en)
            Constants.ENDPOINT_MENU_LANG_SV -> menuLangText?.text = getString(R.string.prefs_menu_lang_sv)
        }

    }
}
