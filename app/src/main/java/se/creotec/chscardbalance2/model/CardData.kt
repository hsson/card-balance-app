// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName

class CardData {
    @SerializedName("card_number")
    var cardNumber: String? = null
    @SerializedName("full_name")
    var ownerName: String? = null
    @SerializedName("email")
    var ownerEmail: String? = null
    @SerializedName("balance")
    var cardBalance: Double = 0.0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as CardData
        return cardBalance == that.cardBalance && cardNumber == that.cardNumber && ownerName == that.ownerName && ownerEmail == that.ownerEmail
    }

    override fun hashCode(): Int {
        var result: Int = cardNumber!!.hashCode()
        val temp: Long = cardBalance.toLong()
        result = 31 * result + (ownerName?.hashCode() ?: 0)
        result = 31 * result + (ownerEmail?.hashCode() ?: 0)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        return result
    }

    override fun toString(): String {
        return "CardData{" +
                "cardNumber='" + cardNumber + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", ownerEmail='" + ownerEmail + '\'' +
                ", cardBalance=" + cardBalance +
                '}'
    }
}
