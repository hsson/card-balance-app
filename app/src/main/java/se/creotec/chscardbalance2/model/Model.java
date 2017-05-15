// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

import se.creotec.chscardbalance2.BuildConfig;
import se.creotec.chscardbalance2.Constants;

public final class Model implements IModel {
    private CardData cardData;
    private MenuData menuData;
    private Set<OnCardDataChangedListener> cardDataChangedListeners;
    private Set<OnMenuDataChangedListener> menuDataChangedListeners;

    private long cardLastTimeUpdated;
    private String preferredMenuLanguage;

    public Model() {
        this.cardDataChangedListeners = new HashSet<>();
        this.menuDataChangedListeners = new HashSet<>();
        this.cardData = new CardData();
        this.menuData = new MenuData();
        this.cardLastTimeUpdated = -1;
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
    public void setCardLastTimeUpdated(long lastUpdated) {
        if (lastUpdated >= 0) {
            this.cardLastTimeUpdated = lastUpdated;
        }
    }

    @Override
    public long getCardLastTimeUpdate() {
        return this.cardLastTimeUpdated;
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

    @Override
    public void setPreferredMenuLanguage(String language) {
        if (language != null) {
            switch (language) {
                case Constants.ENDPOINT_MENU_LANG_EN:
                case Constants.ENDPOINT_MENU_LANG_SV:
                    this.preferredMenuLanguage = language;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getPreferredMenuLanguage() {
        return this.preferredMenuLanguage;
    }

    @Override
    public String getQuickChargeURL() {
        return BuildConfig.BACKEND_URL + Constants.ENDPOINT_CHARGE;
    }

}
