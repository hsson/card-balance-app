// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.GlobalState;
import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.model.CardData;
import se.creotec.chscardbalance2.service.BalanceService;
import se.creotec.chscardbalance2.service.MenuService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalState global = ((GlobalState) getApplication());

        String name = global.getModel().getCardData().getOwnerName();
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();

        Intent updateBalanceIntent = new Intent(this, BalanceService.class);
        updateBalanceIntent.setAction(Constants.ACTION_UPDATE_CARD);
        System.out.println("Starting balance service");
        this.startService(updateBalanceIntent);

        Intent updateMenuIntent = new Intent(this, MenuService.class);
        updateMenuIntent.setAction(Constants.ACTION_UPDATE_MENU);
        System.out.println("Starting menu service");
        this.startService(updateMenuIntent);

        CardData card = global.getModel().getCardData();
        card.setOwnerName("John Doe");
        global.getModel().setCardData(card);
        global.saveCardData();
    }
}
