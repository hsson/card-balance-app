// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.service.BalanceService
import se.creotec.chscardbalance2.service.MenuService
import se.creotec.chscardbalance2.util.CardNumberMask
import se.creotec.chscardbalance2.util.NotificationsHelper
import se.creotec.chscardbalance2.util.Util

class SettingsActivity : AppCompatActivity() {

    var cardNumberContainer: View? = null
    var cardNumberText: TextView? = null

    var menuLangContainer: View? = null
    var menuLangText: TextView? = null

    var toggleLowBalanceContainer: View? = null
    var toggleLowBalanceNotifications: SwitchCompat? = null
    var lowBalanceLimitContainer: View? = null
    var lowBalanceLimitText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val global = application as GlobalState
        var formattedNumber = Util.formatCardNumber(global.model.cardData.cardNumber)

        cardNumberContainer = findViewById(R.id.settings_card_number)
        cardNumberContainer?.setOnClickListener {
            val dialog = MaterialDialog.Builder(this)
                    .title(R.string.prefs_card_number)
                    .inputType(InputType.TYPE_CLASS_NUMBER)
                    .input(getString(R.string.card_number_hint), formattedNumber, {dialog, input ->
                        if (input.toString().replace(" ", "").length == Constants.CARD_NUMBER_LENGTH) {
                            dialog.getActionButton(DialogAction.POSITIVE).isEnabled = true
                            formattedNumber = input.toString()
                        } else {
                            dialog.getActionButton(DialogAction.POSITIVE).isEnabled = false
                        }
                    })
                    .onPositive({_,_ ->
                        setCardNumber(formattedNumber.replace(" ", ""), true)
                    })
                    .alwaysCallInputCallback()
                    .negativeText(R.string.action_cancel)
                    .positiveText(R.string.action_save)
                    .build()

            dialog.inputEditText?.let {
                val filters = Array<InputFilter>(1, { _ -> InputFilter.LengthFilter(Constants.CARD_NUMBER_LENGTH + 3) })
                it.filters = filters
                it.addTextChangedListener(CardNumberMask())
            }
            dialog.show()
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


        lowBalanceLimitContainer = findViewById(R.id.settings_low_balance_limit)
        toggleLowBalanceContainer = findViewById(R.id.settings_enable_low_balance_parent)
        toggleLowBalanceNotifications = findViewById(R.id.settings_enable_low_balance_switch) as SwitchCompat
        lowBalanceLimitText = findViewById(R.id.settings_low_balance_limit_text) as TextView


        val lowBalanceLimitString = getString(R.string.currency_suffix, global.model.notifications.lowBalanceNotificationLimit.toString())
        lowBalanceLimitText?.text = lowBalanceLimitString
        toggleLowBalanceNotifications?.let {
            it.isChecked = global.model.notifications.isLowBalanceNotificationsEnabled
            notificationsEnabledToggled(it.isChecked)
            it.setOnCheckedChangeListener { _, checked -> notificationsEnabledToggled(checked, savePreference = true) }
        }
        toggleLowBalanceContainer?.setOnClickListener { toggleLowBalanceNotifications?.toggle() }

        lowBalanceLimitContainer?.setOnClickListener {
            val dialog = MaterialDialog.Builder(this)
                    .title(R.string.prefs_notifications_low_balance_label)
                    .customView(R.layout.dialog_number_picker, false)
                    .negativeText(R.string.action_cancel)
                    .positiveText(R.string.action_save)
                    .onPositive { dialog, action->
                        if (action == DialogAction.POSITIVE) {
                            val numberPicker = dialog.customView?.findViewById(R.id.dialog_notify_number_picker) as NumberPicker
                            setLowBalanceLimit(numberPicker.value, savePreference = true)
                        }
                    }
                    .build()
            val numberPicker = dialog.customView?.findViewById(R.id.dialog_notify_number_picker) as NumberPicker
            numberPicker.minValue = Constants.PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_MIN
            numberPicker.maxValue = Constants.PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_MAX
            numberPicker.wrapSelectorWheel = false
            numberPicker.value = global.model.notifications.lowBalanceNotificationLimit
            dialog.show()
        }


        setCardNumber(global.model.cardData.cardNumber)
        setMenuLang(global.model.preferredMenuLanguage)
    }

    private fun setLowBalanceLimit(limit: Int, savePreference: Boolean = false) {
        val limitToSet: Int
        if (limit < Constants.PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_MIN ||
                limit > Constants.PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_MAX) {
            limitToSet = Constants.PREFS_NOTIFICATION_LOW_BALANCE_LIMIT_DEFAULT
        } else {
            limitToSet = limit
        }

        val lowBalanceLimitString = getString(R.string.currency_suffix, limitToSet.toString())
        lowBalanceLimitText?.text = lowBalanceLimitString
        if (savePreference) {
            val global = application as GlobalState
            global.model.notifications.lowBalanceNotificationLimit = limitToSet
            global.saveNotificationData()
        }
    }

    private fun notificationsEnabledToggled(enabled: Boolean, savePreference: Boolean = false) {
        lowBalanceLimitContainer?.let {
            it.isClickable = enabled
            it.isFocusable = enabled
            if (enabled) {
                it.alpha = 1f
            } else {
                it.alpha = 0.2f
            }
        }

        if (savePreference) {
            if (!enabled) {
                NotificationsHelper.cancelAll(this)
            }
            val global = application as GlobalState
            global.model.notifications.isLowBalanceNotificationsEnabled = enabled
            global.saveNotificationData()
        }
    }

    private fun setCardNumber(cardNumber: String?, savePreference: Boolean = false) {
        if (cardNumber == null) {
            return
        }
        val number = Util.formatCardNumber(cardNumber)
        cardNumberText?.text = number
        if (savePreference) {
            val global = application as GlobalState
            global.model.cardData.cardNumber = cardNumber
            global.saveCardData()
            val updateCardIntent = Intent(this, BalanceService::class.java)
            updateCardIntent.action = Constants.ACTION_UPDATE_CARD
            startService(updateCardIntent)
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
            val updateMenuIntent = Intent(this, MenuService::class.java)
            updateMenuIntent.action = Constants.ACTION_UPDATE_MENU
            startService(updateMenuIntent)
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
