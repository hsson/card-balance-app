// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MenuData {
    @SerializedName("language")
    private String language;
    @SerializedName("menu")
    private List<Restaurant> menu;

    public MenuData() {
        super();
        menu = new ArrayList<>();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Restaurant> getMenu() {
        return menu;
    }

    public void setMenu(List<Restaurant> menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuData menuData = (MenuData) o;

        if (!language.equals(menuData.language)) return false;
        return menu.equals(menuData.menu);

    }

    @Override
    public int hashCode() {
        int result = language.hashCode();
        result = 31 * result + menu.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MenuData{" +
                "language='" + language + '\'' +
                ", menu=" + menu +
                '}';
    }
}
