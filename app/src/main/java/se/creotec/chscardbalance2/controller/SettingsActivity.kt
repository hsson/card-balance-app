package se.creotec.chscardbalance2.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.util.Util

class SettingsActivity : AppCompatActivity() {

    var cardNumberContainer: View? = null
    var cardNumberText: TextView? = null

    var menuLangContainer: View? = null
    var menuLangText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val global = application as GlobalState

        cardNumberContainer = findViewById(R.id.settings_card_number)
        cardNumberContainer?.setOnClickListener {
            Toast.makeText(this, "YO", Toast.LENGTH_LONG).show()
        }

        cardNumberText = findViewById(R.id.settings_card_number_text) as TextView

        menuLangContainer = findViewById(R.id.settings_menu_lang)
        menuLangContainer?.setOnClickListener {
            MaterialDialog.Builder(this)
                    .title(R.string.prefs_menu_lang)
                    .items(R.array.prefs_menu_lang_list)
                    .itemsCallbackSingleChoice(getDefaultLangIndex(global.model.preferredMenuLanguage), {_, _, which, _ ->
                        when (which) {
                            0 -> setMenuLang(Constants.ENDPOINT_MENU_LANG_EN, true)
                            1 -> setMenuLang(Constants.ENDPOINT_MENU_LANG_SV, true)
                        }
                        true
                    })
                    .negativeText(R.string.action_cancel)
                    .show()

        }
        menuLangText = findViewById(R.id.settings_menu_lang_text) as TextView



        setCardNumber(global.model.cardData.cardNumber)
        setMenuLang(global.model.preferredMenuLanguage)


    }

    private fun setCardNumber(cardNumber: String?, savePreference: Boolean = false) {
        if (cardNumber == null) {
            return
        }
        val number = Util.formatCardNumber(cardNumber)
        cardNumberText?.text = number
        if (savePreference) {
            // TODO: Save preference
        }
    }

    private fun setMenuLang(lang: String?, savePreference: Boolean = false) {
        if (lang == null) {
            return
        }
        when (lang) {
            Constants.ENDPOINT_MENU_LANG_EN -> menuLangText?.text = getString(R.string.prefs_menu_lang_en)
            Constants.ENDPOINT_MENU_LANG_SV -> menuLangText?.text = getString(R.string.prefs_menu_lang_sv)
            else -> return
        }
        if (savePreference) {
            val global = application as GlobalState
            global.model.preferredMenuLanguage = lang
            global.saveMenuData()
        }
    }

    private fun getDefaultLangIndex(lang: String?): Int {
        if (lang == null) {
            return -1
        }

        when (lang) {
            Constants.ENDPOINT_MENU_LANG_EN -> return 0
            Constants.ENDPOINT_MENU_LANG_SV -> return 1
            else -> return 0
        }
    }
}
