// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

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

}
