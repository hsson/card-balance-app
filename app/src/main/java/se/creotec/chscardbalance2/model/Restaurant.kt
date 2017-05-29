// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class Restaurant(@SerializedName("name")
                 var name: String) : Comparable<Restaurant> {
    @SerializedName("image_url")
    var imageUrl: String? = null
    @SerializedName("website_url")
    var websiteUrl: String? = null
    @SerializedName("rating")
    var rating: Float = 0.0f
    @SerializedName("avg_price")
    var averagePrice: Int = 0
    @SerializedName("dishes")
    var dishes: List<Dish> = ArrayList()
    @SerializedName("open_hours")
    var openHours: List<OpenHour> = ArrayList()

    fun isClosed(): Boolean {
        return dishes.isEmpty()
    }

    override fun compareTo(other: Restaurant) = compareValuesBy(this, other, {it.isClosed()})

    override fun toString(): String {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", dishes=" + dishes +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Restaurant

        if (name != other.name) return false
        if (imageUrl != other.imageUrl) return false
        if (websiteUrl != other.websiteUrl) return false
        if (rating != other.rating) return false
        if (averagePrice != other.averagePrice) return false
        if (dishes != other.dishes) return false
        if (openHours != other.openHours) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + (websiteUrl?.hashCode() ?: 0)
        result = 31 * result + rating.hashCode()
        result = 31 * result + averagePrice
        result = 31 * result + dishes.hashCode()
        result = 31 * result + openHours.hashCode()
        return result
    }
}
