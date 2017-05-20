// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

interface OnCardDataChangedListener {
    fun cardDataChanged(newData: CardData)
}
