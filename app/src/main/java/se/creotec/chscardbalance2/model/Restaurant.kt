// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Restaurant(@SerializedName("name")
                 var name: String) : Comparable<Restaurant> {
    @SerializedName("image_url")
    var imageUrl: String? = null
    @SerializedName("website_url")
    var websiteUrl: String? = null
    @SerializedName("rating")
    var rating: Float = 0.0f
    @SerializedName("dishes")
    var dishes: List<Dish> = ArrayList()

    fun isClosed(): Boolean {
        return dishes.isEmpty()
    }

    override fun compareTo(other: Restaurant) = compareValuesBy(this, other, {it.isClosed()})

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as Restaurant
        return name == that.name && imageUrl == that.imageUrl && dishes == that.dishes && websiteUrl == that.websiteUrl && rating == that.rating
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 29 * result + (imageUrl?.hashCode() ?: 0)
        result = 29 * result + dishes.hashCode()
        result = 29 * result + (websiteUrl?.hashCode() ?: 0)
        result = 29 * result + rating.toInt()
        return result
    }

    override fun toString(): String {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", dishes=" + dishes +
                '}'
    }
}
