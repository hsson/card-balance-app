// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import se.creotec.chscardbalance2.BuildConfig
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.service.AbstractBackendService
import java.util.*

class Model : IModel {
    override val quickChargeURL: String
    override var notifications: NotificationData = NotificationData()
    private val cardDataChangedListeners: MutableSet<OnCardDataChangedListener>
    private val menuDataChangedListeners: MutableSet<OnMenuDataChangedListener>
    private val serviceFailedListeners: MutableSet<IModel.OnServiceFailedListener>

    override var cardData: CardData = CardData()
        set(value) {
            field = value
            notifyCardDataChangedListeners()
        }
    override var menuData: MenuData = MenuData()
        set(value) {
            field = value
            notifyMenuDataChangedListeners()
        }
    override var cardLastTimeUpdated: Long = -1
        set(value) = if (value >= 0) field = value else field = -1
    override var menuLastTimeUpdated: Long = -1
        set(value) = if (value >= 0) field = value else field = -1
    override var preferredMenuLanguage: String = Constants.PREFS_MENU_LANGUAGE_DEFAULT
        set(value) {
            if (isOKLang(value)) field = value
        }

    init {
        cardDataChangedListeners = HashSet<OnCardDataChangedListener>()
        menuDataChangedListeners = HashSet<OnMenuDataChangedListener>()
        serviceFailedListeners = HashSet<IModel.OnServiceFailedListener>()
        quickChargeURL = BuildConfig.BACKEND_URL + Constants.ENDPOINT_CHARGE
    }

    override fun addCardDataListener(listener: OnCardDataChangedListener) {
        cardDataChangedListeners.add(listener)
    }

    override fun notifyCardDataChangedListeners() {
        for (listener in cardDataChangedListeners) {
            listener.cardDataChanged(this.cardData)
        }
    }

    override fun addMenuDataListener(listener: OnMenuDataChangedListener) {
        menuDataChangedListeners.add(listener)
    }

    override fun notifyMenuDataChangedListeners() {
        for (listener in this.menuDataChangedListeners) {
            listener.menuDataChanged(this.menuData)
        }
    }

    override fun addServiceFailedListener(listener: IModel.OnServiceFailedListener) {
        serviceFailedListeners.add(listener)
    }

    override fun notifyServiceFailed(service: AbstractBackendService<*>, error: String) {
        for (listener in serviceFailedListeners) {
            listener.serviceFailed(service, error)
        }
    }

    private fun isOKLang(language: String): Boolean {
        when (language) {
            Constants.ENDPOINT_MENU_LANG_EN -> return true
            Constants.ENDPOINT_MENU_LANG_SV -> return true
            else -> return false
        }
    }
}
