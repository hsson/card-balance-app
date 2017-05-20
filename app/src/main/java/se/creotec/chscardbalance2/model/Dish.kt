// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName

class Dish {
    @SerializedName("title")
    var title: String = ""
    @SerializedName("desc")
    var description: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val dish = other as Dish
        return title == dish.title && description == dish.description
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    override fun toString(): String {
        return "Dish{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}'
    }
}
