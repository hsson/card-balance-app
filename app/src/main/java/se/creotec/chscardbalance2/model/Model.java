// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

public final class Model implements IModel {
    private CardData cardData;
    private MenuData menuData;
    private Set<OnCardDataChangedListener> cardDataChangedListeners;
    private Set<OnMenuDataChangedListener> menuDataChangedListeners;

    public Model() {
        this.cardDataChangedListeners = new HashSet<>();
        this.menuDataChangedListeners = new HashSet<>();
        this.cardData = new CardData();
        this.menuData = new MenuData();
    }

    @Override
    public void setCardData(final CardData data) {
        this.cardData = data;
        notifyCardDataChangedListeners();
    }

    @Override
    public CardData getCardData() {
        return this.cardData;
    }

    @Override
    public void addCardDataListener(@NonNull OnCardDataChangedListener listener) {
        cardDataChangedListeners.add(listener);
    }

    @Override
    public void notifyCardDataChangedListeners() {
        for (OnCardDataChangedListener listener : this.cardDataChangedListeners) {
            listener.notify(this.cardData);
        }
    }

    @Override
    public void setMenuData(final MenuData data) {
        this.menuData = data;
        notifyMenuDataChangedListeners();
    }

    @Override
    public MenuData getMenuData() {
        return this.menuData;
    }

    @Override
    public void addMenuDataListener(@NonNull OnMenuDataChangedListener listener) {
        menuDataChangedListeners.add(listener);
    }

    @Override
    public void notifyMenuDataChangedListeners() {
        for (OnMenuDataChangedListener listener : this.menuDataChangedListeners) {
            listener.notify(this.menuData);
        }
    }

}
