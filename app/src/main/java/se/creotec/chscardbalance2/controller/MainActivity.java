// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import se.creotec.chscardbalance2.BuildConfig;
import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.GlobalState;
import se.creotec.chscardbalance2.R;
import se.creotec.chscardbalance2.model.CardData;
import se.creotec.chscardbalance2.service.BalanceService;
import se.creotec.chscardbalance2.service.MenuService;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton quickChargeFAB;
    private TextView cardOwnerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalState global = ((GlobalState) getApplication());
        setupAppBar();
        setupFAB(global);

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
        cardOwnerName.setText(global.getModel().getCardData().getOwnerName());
        collapsingToolbarLayout.setTitle(global.getModel().getCardData().getCardBalance() + " " + Constants.CARD_CURRENCY_SUFFIX);
    }

    // Sets up the appbar
    private void setupAppBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout =  (CollapsingToolbarLayout) findViewById(R.id.toolbar_collapsing_layout);
        cardOwnerName = (TextView) findViewById(R.id.toolbar_card_name);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        // Fade out name when app bar is collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());
                cardOwnerName.setAlpha(1-percentage*2);
            }
        });
    }

    // Adds action to the FAB
    private void setupFAB(final GlobalState global) {
        quickChargeFAB = (FloatingActionButton) findViewById(R.id.fab_charge_card);
        final Context context = this;
        quickChargeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(getColor(R.color.color_primary));
                builder.setSecondaryToolbarColor(getColor(R.color.color_accent));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(global.getModel().getQuickChargeURL()));
            }
        });
    }
}
