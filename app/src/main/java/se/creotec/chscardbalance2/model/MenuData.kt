// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName
import se.creotec.chscardbalance2.Constants

class MenuData {
    @SerializedName("language")
    var language: String = Constants.PREFS_MENU_LANGUAGE_DEFAULT
    @SerializedName("menu")
    var menu: List<Restaurant> = ArrayList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val menuData = other as MenuData
        return menu == menuData.menu && language == menuData.language
    }

    override fun hashCode(): Int {
        var result = language.hashCode()
        result = 31 * result + menu.hashCode()
        return result
    }

    override fun toString(): String {
        return "MenuData{" +
                "language='" + language + '\'' +
                ", menu=" + menu +
                '}'
    }
}
