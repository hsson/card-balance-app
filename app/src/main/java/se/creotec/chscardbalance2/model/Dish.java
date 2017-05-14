// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model;

import com.google.gson.annotations.SerializedName;

public class Dish {
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dish dish = (Dish) o;

        if (!title.equals(dish.title)) return false;
        return description.equals(dish.description);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
