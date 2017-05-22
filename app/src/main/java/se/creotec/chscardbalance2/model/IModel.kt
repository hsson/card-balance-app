// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import se.creotec.chscardbalance2.service.AbstractBackendService
import kotlin.reflect.KClass

interface IModel {
    var cardData: CardData
    fun addCardDataListener(listener: OnCardDataChangedListener)
    fun notifyCardDataChangedListeners()
    var cardLastTimeUpdated: Long

    var menuData: MenuData
    fun addMenuDataListener(listener: OnMenuDataChangedListener)
    fun notifyMenuDataChangedListeners()
    var preferredMenuLanguage: String
    var menuLastTimeUpdated: Long

    val quickChargeURL: String

    fun addServiceFailedListener(listener: OnServiceFailedListener)
    fun notifyServiceFailed(service: AbstractBackendService<*>, error: String)

    interface OnServiceFailedListener {
        fun serviceFailed(service: AbstractBackendService<*>, error: String)
    }

}
