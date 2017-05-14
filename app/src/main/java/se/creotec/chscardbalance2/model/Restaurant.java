// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    @SerializedName("name")
    private String name;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("dishes")
    private List<Dish> dishes;

    public Restaurant() {
        super();
        dishes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        if (!name.equals(that.name)) return false;
        if (!imageUrl.equals(that.imageUrl)) return false;
        return dishes.equals(that.dishes);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + dishes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}
