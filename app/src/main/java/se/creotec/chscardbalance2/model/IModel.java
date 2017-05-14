// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model;

import android.support.annotation.NonNull;

public interface IModel {
    void setCardData(final CardData data);
    CardData getCardData();
    void addCardDataListener(@NonNull OnCardDataChangedListener listener);
    void notifyCardDataChangedListeners();

    void setMenuData(final MenuData data);
    MenuData getMenuData();
    void addMenuDataListener(@NonNull OnMenuDataChangedListener listener);
    void notifyMenuDataChangedListeners();

}
