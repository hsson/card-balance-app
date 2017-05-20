// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Restaurant(@SerializedName("name")
                 var name: String) : Comparable<Restaurant> {
    @SerializedName("image_url")
    var imageUrl: String? = null
    @SerializedName("dishes")
    var dishes: List<Dish> = ArrayList()

    override fun compareTo(other: Restaurant) = compareValuesBy(this, other, {-it.dishes.size})

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as Restaurant
        return name == that.name && imageUrl == that.imageUrl && dishes == that.dishes
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 29 * result + (imageUrl?.hashCode() ?: 0)
        result = 29 * result + dishes.hashCode()
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
